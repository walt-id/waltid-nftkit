package id.walt.nftkit.erc721Features

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.web3j.crypto.Credentials
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class Erc721StandardTest : StringSpec({

    val enableTest = false

    "Smart contract deployment".config(enabled = enableTest) {
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val result = NftService.deploySmartContractToken(Chain.MUMBAI, deploymentParameter, deploymentOptions)
        val privateKey= WaltIdServices.loadChainConfig().privateKey
        val credentials: Credentials = Credentials.create(privateKey)
        result.contractAddress shouldNotBe  ""
        result.contractAddress shouldNotBe null
        val owner = AccessControlService.owner(Chain.MUMBAI, result.contractAddress)
        owner shouldBe credentials.address
        val checkInfo = NftService.getTokenCollectionInfo(Chain.MUMBAI, result.contractAddress )
        checkInfo.name shouldBe "Metaverse"
        checkInfo.symbol shouldBe "MTV"
    }

    "Minting token".config(enabled=enableTest){
        val nftMetaData = NftMetadata("Ticket 2 description", "Ticket 2", "string", "string","string")
        val mintingParametre = MintingParameter(metadataUri = "", recipientAddress="0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31", metadata = nftMetaData )
        val mintingOption = MintingOptions(MetadataStorageType.ON_CHAIN)
        val result = NftService.mintToken(Chain.MUMBAI, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",mintingParametre, mintingOption)
        val newNFT = NftService.getNftMetadata(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", result.tokenId!!)
        result.tokenId shouldNotBe null
        newNFT shouldBe nftMetaData
    }


    "Verifying nft metadata".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(3)
        val result = NftService.getNftMetadata(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )
        result.name shouldBe "Ticket #1"
        result.description shouldBe "Ticket #1 Description"

    }

    "Verifying Metadata URI".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(10)
        val result = NftService.getNftMetadataUri(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )
        result shouldBe "ipfs://bafyreiebsxbmwmgrzlrhlpwj2mmz5j64vsfojfy7lviu22unkkmjdhfqt4/metadata.json"
    }

    "Verifying balance of address".config(enabled=enableTest){
        val result = NftService.balanceOf(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            "0xe895D59e84d0E77a8DaEaA55547528406C5a1314")
        result shouldBe BigInteger.valueOf(2)
    }

    "Verifying owner of a token".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(35)
        val result = NftService.ownerOf(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
        tokenid)
        result shouldBe "0xe895d59e84d0e77a8daeaa55547528406c5a1314"
    }

    "Verifying token collection info".config(enabled=enableTest){
        val result = NftService.getTokenCollectionInfo(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.name shouldBe "Ticket"
        result.symbol shouldBe "TK"
    }

    "Verifying account NFTs by Alchmy".config(enabled=enableTest){
        val result = NftService.getAccountNFTsByAlchemy(Chain.MUMBAI,
            "0xe895D59e84d0E77a8DaEaA55547528406C5a1314" )
        result[0].id.tokenId shouldBe "35"
        result[1].id.tokenId shouldBe "36"
    }

    "Setting metadata".config(enabled=enableTest){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val result = NftService.updateMetadata(chain=Chain.MUMBAI,
            contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            tokenId="5",
            key= "t1",
            value= currentDate,
            signedAccount = null
            )
        val check = NftService.getNftMetadata(chain=Chain.MUMBAI, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenId=BigInteger.valueOf(5))
        check.attributes!![0].value shouldBe currentDate
        result.transactionId shouldNotBe null
    }

    "Verifying IPFS metadata using NFTs storage".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(10)
        var uri = NftService.getNftMetadataUri(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" ,tokenid)
        val result = NftService.getIPFSMetadataUsingNFTStorage(uri)
        result.description shouldBe "string"
        result.name shouldBe "string"
        result.image shouldBe "string"
        result.external_url shouldBe "string"
        result.attributes?.get(0)!!.trait_type shouldBe "string"
        result.attributes?.get(0)!!.value shouldBe "15/7/2022 10:30:07"
    }

    "Adding file to ipfs nft storage".config(enabled=enableTest){
        val byteArray = Files.readAllBytes(Paths.get("src/test/resources/nft-sample/image.jpg"))
        val result = NftService.addFileToIpfsUsingNFTStorage(byteArray)
        println(result.value.cid)
        result.value.cid shouldNotBe null
        result.ok shouldBe true
    }


})