package id.walt.nftkit.erc721Features

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths

class Erc721StandardTest : StringSpec({

    val enableTest = false

    "Smart contract deployment".config(enabled = enableTest) {
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val result = NftService.deploySmartContractToken(Chain.MUMBAI, deploymentParameter, deploymentOptions)
        result.contractAddress shouldNotBe  ""
        result.contractAddress shouldNotBe null
    }

    "minting token".config(enabled=enableTest){
        val nftMetaData = NftMetadata("string", "string", "string", "string","string")
        val mintingParametre = MintingParameter(metadataUri = "string", recipientAddress="0x59Fbfc9ad3E3b99ae8C480590908f09019667181", metadata = nftMetaData )
        val mintingOption = MintingOptions(MetadataStorageType.ON_CHAIN)
        val result = NftService.mintToken(Chain.MUMBAI, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",mintingParametre, mintingOption)
        println(result.transactionResponse)
        result.tokenId shouldNotBe null
    }

    "get nft metadatatest".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(4)

        val result = NftService.getNftMetadata(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )

        result shouldNotBe null
    }

    "get metadata uri".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(4)
        val result = NftService.getNftMetadataUri(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )
        result shouldNotBe null
        result shouldNotBe ""
    }

    "get balance of address".config(enabled=enableTest){
        val result = NftService.balanceOf(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            "0x59Fbfc9ad3E3b99ae8C480590908f09019667181")
        result shouldNotBe null
    }

    "get owner of a token test".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(4)

        val result = NftService.ownerOf(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
        tokenid)
        result shouldNotBe null
        result shouldNotBe ""
    }

    "get token collection info".config(enabled=enableTest){
        val result = NftService.getTokenCollectionInfo(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.name shouldNotBe null
        result.name shouldNotBe ""
    }

    "get account nfts by alchmy".config(enabled=enableTest){
        val result = NftService.getAccountNFTsByAlchemy(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.size shouldNotBe null
    }

    "update meta data".config(enabled=enableTest){
        val result = NftService.updateMetadata(chain=Chain.MUMBAI,
            contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            tokenId="4",
            key= "string",
            value= "string",
            signedAccount = null
            )
        result.transactionId shouldNotBe null
    }

    "get IPFS meta data sing NFTs storage".config(enabled=true){
        val tokenid = BigInteger.valueOf(10)
        var uri = NftService.getNftMetadataUri(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" ,tokenid)
        val result = NftService.getIPFSMetadataUsingNFTStorage(uri)

        result.description shouldBe "string"
        result.name shouldBe "string"
        result.image shouldBe "string"
        result.external_url shouldBe "string"
        result.attributes?.get(0)!!.trait_type shouldBe "string"
        result.attributes?.get(0)!!.value shouldBe "string"

    }

    "add file to ipfs nft storage".config(enabled=enableTest){
        val byteArray = Files.readAllBytes(Paths.get("C:\\Users\\ahmed\\Desktop\\sa7eHXGY_400x400.jpg"))
        val result = NftService.addFileToIpfsUsingNFTStorage(byteArray)
        result.ok shouldBe true

    }


})