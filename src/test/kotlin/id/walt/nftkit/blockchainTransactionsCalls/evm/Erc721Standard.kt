package id.walt.nftkit.blockchainTransactionsCalls.evm

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

class Erc721Standard : StringSpec({


    "Smart contract deployment".config() {
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val result = NftService.deploySmartContractToken(EVMChain.AMOY, deploymentParameter, deploymentOptions)
        val privateKey= WaltIdServices.loadChainConfig().privateKey
        val credentials: Credentials = Credentials.create(privateKey)
        result.contractAddress shouldNotBe  ""
        result.contractAddress shouldNotBe null
        val owner = AccessControlService.owner(EVMChain.AMOY, result.contractAddress)
        owner shouldBe credentials.address
        val checkInfo = NftService.getTokenCollectionInfo(EVMChain.AMOY, result.contractAddress )
        checkInfo.name shouldBe "Metaverse"
        checkInfo.symbol shouldBe "MTV"
    }

    "Minting token".config(){
        val nftMetaData = NftMetadata("Ticket 2 description", "Ticket 2", "string", "string","string")
        val mintingParametre = MintingParameter(metadataUri = "", recipientAddress="0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31", metadata = nftMetaData )
        val mintingOption = MintingOptions(MetadataStorageType.ON_CHAIN)
        val result = NftService.mintToken(EVMChain.AMOY, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",mintingParametre, mintingOption , false)
        val newNFT = NftService.getNftMetadata(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", result.tokenId!!)
        result.tokenId shouldNotBe null
        newNFT shouldBe nftMetaData
    }

    "Setting metadata".config(){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val result = NftService.updateMetadata(chain=EVMChain.AMOY,
            contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            tokenId="5",
            key= "t1",
            value= currentDate,
            signedAccount = null
            )
        val check = NftService.getNftMetadata(chain=EVMChain.AMOY, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenId=BigInteger.valueOf(5))
        check.attributes!![0].value shouldBe currentDate
        result.transactionId shouldNotBe null
    }

    "Adding file to ipfs nft storage".config(){
        val byteArray = Files.readAllBytes(Paths.get("src/test/resources/nft-sample/image.jpg"))
        val result = NftService.addFileToIpfsUsingNFTStorage(byteArray)
        println(result.value.cid)
        result.value.cid shouldNotBe null
        result.ok shouldBe true
    }


})