package id.walt.nftkit.utilis

import id.walt.nftkit.Values
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.MetadataStorageType
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.services.WaltIdServices
import org.web3j.protocol.core.methods.response.TransactionReceipt

object Common {

    fun getTransactionResponse(chain: Chain, transactionReceipt: TransactionReceipt): TransactionResponse {
        val url = WaltIdServices.getBlockExplorerUrl(chain)
        return TransactionResponse(transactionReceipt.transactionHash, "$url/tx/${transactionReceipt.transactionHash}")
    }


    fun getChain(chain: String): Chain{
        return chain.let {
            if (it.isEmpty()){
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }
    }

    fun getMetadataType(uri: String): MetadataStorageType {
        if(uri.contains("data:application/json;base64", true)){
            return MetadataStorageType.ON_CHAIN
        }else{
            return MetadataStorageType.OFF_CHAIN
        }
    }

    fun getNetworkBlockExplorerApiUrl(chain: Chain): String{
        return when (chain) {
            Chain.ETHEREUM -> Values.ETHEREUM_MAINNET_SCAN_API_URL
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_SCAN_API_URL
            Chain.ROPSTEN -> Values.ETHEREUM_TESTNET_ROPSTEN_SCAN_API_URL
            Chain.POLYGON -> Values.POLYGON_MAINNET_SCAN_API_URL
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_SCAN_API_URL
        }
    }

    fun getNetworkBlockExplorerApiKey(chain: Chain): String{
        return when (chain) {
            Chain.ETHEREUM -> WaltIdServices.loadApiKeys().apiKeys.ethereumBlockExplorer
            Chain.RINKEBY -> WaltIdServices.loadApiKeys().apiKeys.ethereumBlockExplorer
            Chain.ROPSTEN -> WaltIdServices.loadApiKeys().apiKeys.ethereumBlockExplorer
            Chain.POLYGON -> WaltIdServices.loadApiKeys().apiKeys.polygonBlockExplorer
            Chain.MUMBAI -> WaltIdServices.loadApiKeys().apiKeys.polygonBlockExplorer
        }
    }
}

