package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.rest.UpdateTokenURIRequest
import id.walt.nftkit.services.*
import id.walt.nftkit.services.AccessControl.ROLE_BASED_ACCESS_CONTROL
import id.walt.nftkit.utilis.providers.ProviderFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.web3j.crypto.Credentials
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.io.File
import smart_contract_wrapper.CustomOwnableERC721
import java.math.BigInteger
import kotlin.text.toByteArray

suspend fun main() {
    println("\n\n\n")


    /* /////////// */
    /* /////////// */
    /* /////////// */
    NftKitApi.start()


    /*val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "valid", value = "true")
    val attributes = mutableListOf(attribute1)
    val nftMetadata = NftMetadata(name = "Bored apes", description = "Bored apes#01", image = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/advisor/in/wp-content/uploads/2022/03/monkey-g412399084_1280.jpg", attributes = attributes)
    val metadata= Json.encodeToString(nftMetadata)
    println(metadata)
    println("Store metadata result is: " + nftStorageAddMetadataTest(metadata))*/

/*    val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6"*//*, display_type = null*//*)
    val attribute2 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6"*//*, display_type = null*//*)

    val attributes = mutableListOf(attribute1, attribute2)
    val nftMetadata : NftMetadata = NftMetadata(name = "m6", description = "m6", image = "", attributes = attributes)*/

    /*println(erc721TokenStandard.supportsInterface(Chain.RINKEBY,"0xc831de165bD2356230e60DF549324034dB5A3BD5"))*/

    /* val mintingOptions : MintingOptions = MintingOptions(TokenStandard.ERC721, MetadataStorageType.ON_CHAIN, null)
    val mintingParameter: MintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74",nftMetadata)
    val ms: MintingResponse = NftService.mintToken(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", mintingParameter, mintingOptions)
*/
}





suspend fun nftStorageUploadFileTest(): String {
    return runBlocking {
        val file = File("metadata.json") // WARNING -> metadata.json is not an image

        val res = NftService.client.post("https://api.nft.storage/upload") {
            contentType(ContentType.Image.Any)
            setBody(file.readBytes())
        }.bodyAsText()

        return@runBlocking res
    }
}




fun updateOwnableScMetadataUseCase() {//0x856b30a1068659d29bc87d851b5df10bbf0137a8  mumbai
    //Deploy new Ownable smart contract
    //if you have already a deployed smart contract, you can skip this part
    val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
    val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
    val sc = NftService.deploySmartContractToken(Chain.MUMBAI, deploymentParameter, deploymentOptions)
    println("Smart contrcat Address: ${sc.contractAddress}")

    val erc721Wrapper = loadOwnableContract(Chain.MUMBAI, sc.contractAddress)

    //Mint new token
    //if you have already a minted token, you can skip this part
    val attribute1: NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "valid", value = "true")
    val attributes = mutableListOf(attribute1)
    val nftMetadata = NftMetadata(
        name = "Bored apes",
        description = "Bored apes#01",
        image = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/advisor/in/wp-content/uploads/2022/03/monkey-g412399084_1280.jpg",
        attributes = attributes
    )
    val mintingOptions = MintingOptions(MetadataStorageType.ON_CHAIN)
    val mintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74", nftMetadata)
    val mintResponse = NftService.mintToken(Chain.MUMBAI, sc.contractAddress, mintingParameter, mintingOptions)
    println("New Token Id: ${mintResponse.tokenId}")


    //Update Metadata
    //This is where actually we take an existing metadata to update it
    nftMetadata.attributes?.get(0)!!.value = "False"
    val updateTokenURIRequest = UpdateTokenURIRequest(null, nftMetadata, MetadataStorageType.ON_CHAIN)
    ExtensionsService.setTokenURI(Chain.MUMBAI, sc.contractAddress, mintResponse.tokenId.toString(), updateTokenURIRequest)
    val verifyUpdateMetadata = NftService.getNftMetadata(Chain.MUMBAI, sc.contractAddress, mintResponse.tokenId!!)
    println("Fetch new Metadata: ")
    println(verifyUpdateMetadata)


}

fun updateMetadataUseCase(){
    //Deploy new RBAC smart contract
    val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
    val deploymentOptions = DeploymentOptions(ROLE_BASED_ACCESS_CONTROL, TokenStandard.ERC721)
    val sc = NftService.deploySmartContractToken(Chain.RINKEBY, deploymentParameter, deploymentOptions)
    println("Smart contrcat Address: ${sc.contractAddress}")

    //Load smart contract
    val erc721Wrapper = loadRbacContract(Chain.RINKEBY, sc.contractAddress)

    //Grant MINTER_ROLE to an account
    val credential: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
    AccessControlService.grantRole(Chain.RINKEBY, sc.contractAddress, "MINTER_ROLE", credential.address)
    val hasMinterRole = AccessControlService.hasRole(Chain.RINKEBY, sc.contractAddress, "MINTER_ROLE", credential.address)
    println("Assigned -MINTER_ROLE- role verification: $hasMinterRole")

    //Mint new token
    val attribute1: NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "valid", value = "true")
    val attributes = mutableListOf(attribute1)
    val nftMetadata = NftMetadata(
        name = "Bored apes",
        description = "Bored apes#01",
        image = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/advisor/in/wp-content/uploads/2022/03/monkey-g412399084_1280.jpg",
        attributes = attributes
    )
    val mintingOptions = MintingOptions(MetadataStorageType.ON_CHAIN)
    val mintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74", nftMetadata)
    val mintResponse = NftService.mintToken(Chain.RINKEBY, sc.contractAddress, mintingParameter, mintingOptions)
    println("New Token Id: ${mintResponse.tokenId}")
    val verifyMetadata = NftService.getNftMetadata(Chain.RINKEBY, sc.contractAddress, mintResponse.tokenId!!)
    println("Fetch Metadata: ")
    println(verifyMetadata)

    //Grant MINTER_ROLE to an account
    AccessControlService.grantRole(Chain.RINKEBY, sc.contractAddress, "METADATA_UPDATER_ROLE", credential.address)
    val METADATA_UPDATER_ROLE =
        AccessControlService.hasRole(Chain.RINKEBY, sc.contractAddress, "METADATA_UPDATER_ROLE", credential.address)
    println("Assigned -METADATA_UPDATER_ROLE- role verification: $METADATA_UPDATER_ROLE")

    //Update Metadata
    nftMetadata.attributes?.get(0)!!.value = "False"
    val updateTokenURIRequest = UpdateTokenURIRequest(null, nftMetadata, MetadataStorageType.ON_CHAIN)
    ExtensionsService.setTokenURI(Chain.RINKEBY, sc.contractAddress, mintResponse.tokenId.toString(), updateTokenURIRequest)
    val verifyUpdateMetadata = NftService.getNftMetadata(Chain.RINKEBY, sc.contractAddress, mintResponse.tokenId)
    println("Fetch new Metadata: ")
    println(verifyUpdateMetadata)


}

private fun loadRbacContract(chain: Chain, address: String): AccessControl {
    val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

    val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

    val gasProvider: ContractGasProvider = WaltIdGasProvider

    return when (chain) {
        Chain.POLYGON, Chain.MUMBAI -> {
            val chainId = when (chain) {
                Chain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
                else -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            }

            val transactionManager: TransactionManager = RawTransactionManager(web3j, credentials, chainId)

            AccessControl.load(address, web3j, transactionManager, gasProvider)
        }
        else -> AccessControl.load(address, web3j, credentials, gasProvider)
    }
}

private fun loadOwnableContract(chain: Chain, address: String): CustomOwnableERC721? {
    val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

    val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

    val gasProvider: ContractGasProvider = WaltIdGasProvider

    return when (chain) {
        Chain.POLYGON, Chain.MUMBAI -> {
            val chainId = when (chain) {
                Chain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
                else -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            }

            val transactionManager: TransactionManager = RawTransactionManager(web3j, credentials, chainId)

            CustomOwnableERC721.load(address, web3j, transactionManager, gasProvider)
        }
        else -> CustomOwnableERC721.load(address, web3j, credentials, gasProvider)
    }
}
