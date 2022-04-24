package id.walt.nftkit

import id.walt.nftkit.rest.UpdateTokenURIRequest
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.providers.ProviderFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.web3j.crypto.Credentials
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.io.File

suspend fun main() {

    // Store metadata API
    println("Result is: " + nftStorageAddMetadataTest("{\"description\":\"string\",\"name\":\"string\",\"image\":\"string\",\"attributes\":[{\"trait_type\":\"string\",\"value\":\"string\"}]}"))

    // println("\n\n\n\n\n\n\n\n\n")

    // Upload file API
    // println(nftStorageUploadFileTest())

    /* /////////// */
    /* /////////// */
    /* /////////// */
    //NftKitApi.start()
    //updateMetadataUseCase()

    //val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "valid", value = "true")
    //val attributes = mutableListOf(attribute1)
    //val nftMetadata = NftMetadata(name = "Bored apes", description = "Bored apes#01", image = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/advisor/in/wp-content/uploads/2022/03/monkey-g412399084_1280.jpg", attributes = attributes)


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

        val file = File("metadata.json")
        //file.forEachLine { println(it) }
        val res = NftService.client.submitFormWithBinaryData(
            url = "https://api.nft.storage/upload",
            formData = formData {
                append("image", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
            }
        ).bodyAsText()


        /*val result = NftService.client.post("https://api.nft.storage/upload") {
            body = MultiPartFormDataContent(
                formData {
                    append("file", file.readBytes(), Headers.build {
                        //append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                },
            )
        }*/

        return@runBlocking res
    }
}


suspend fun nftStorageAddMetadataTest(metadata: String): String {

    return runBlocking {
        val res = NftService.client.submitForm(
            url = "https://api.nft.storage/store",
            formParameters = Parameters.build {
                append("meta", metadata)
            }
        ).bodyAsText()

        return@runBlocking res
    }

}

suspend fun nftStorageReadMetadataTest(): String {
    return runBlocking {


        val result = NftService.client.get("https://api.nft.storage/") {
            headers {
                append(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQ6ZXRocjoweDYwNDNEYThENjU2RTU3NTg2ZDk3MkM1ZDM5RUNENzI1NTNCM2Q1NjAiLCJpc3MiOiJuZnQtc3RvcmFnZSIsImlhdCI6MTY1MDUzNzUzNjk2OSwibmFtZSI6Ik5GVHMifQ.MBK_rahk6e6fCTheE7qLJeZ_OIyKGkbP63aVLPas1sw"
                )
            }
        }
            .body<String>()
        return@runBlocking result
    }
}


fun updateMetadataUseCase() {
    //Deploy new RBAC smart contract
    val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
    val deploymentOptions = DeploymentOptions(AccessControl.ROLE_BASED_ACCESS_CONTROL, TokenStandard.ERC721)
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

private fun loadRbacContract(chain: Chain, address: String): smart_contract_wrapper.AccessControl {
    val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

    val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

    val gasProvider: ContractGasProvider = WaltIdGasProvider

    if (chain == Chain.POLYGON || chain == Chain.MUMBAI) {
        val chainId: Long
        if (chain == Chain.POLYGON) {
            chainId = Values.POLYGON_MAINNET_CHAIN_ID
        } else {
            chainId = Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
        }
        val transactionManager: TransactionManager = RawTransactionManager(
            web3j, credentials, chainId
        )
        return smart_contract_wrapper.AccessControl.load(address, web3j, transactionManager, gasProvider)
    } else {
        return smart_contract_wrapper.AccessControl.load(address, web3j, credentials, gasProvider)
    }
}
