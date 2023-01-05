package id.walt.nftkit.utilis

import id.walt.nftkit.Values
import id.walt.nftkit.services.*
import org.web3j.protocol.core.methods.response.TransactionReceipt

object Common {

    fun getTransactionResponse(chain: EVMChain, transactionReceipt: TransactionReceipt): TransactionResponse {
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

    fun getTezosChain(chain: String): TezosChain{
        return chain.let {
            if (it.isEmpty()){
                throw Exception("No chain defined")
            }
            TezosChain.valueOf(it.uppercase())
        }
    }

    fun getFa2SmartContractType(type: String): Fa2SmartContractType{
        return type.let {
            if (it.isEmpty()){
                throw Exception("No type defined")
            }
            Fa2SmartContractType.valueOf(it.uppercase())
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
            Chain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_SCAN_API_URL
            Chain.POLYGON -> Values.POLYGON_MAINNET_SCAN_API_URL
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_SCAN_API_URL
            Chain.TEZOS -> throw Exception("Tezos is not supported")
            Chain.GHOSTNET -> throw Exception("Ghostnet is not supported")
        }
    }

    fun getNetworkBlockExplorerApiKey(chain: Chain): String{
        return when (chain) {
            Chain.ETHEREUM -> WaltIdServices.loadApiKeys().apiKeys.ethereumBlockExplorer
            Chain.GOERLI -> WaltIdServices.loadApiKeys().apiKeys.ethereumBlockExplorer
            Chain.POLYGON -> WaltIdServices.loadApiKeys().apiKeys.polygonBlockExplorer
            Chain.MUMBAI -> WaltIdServices.loadApiKeys().apiKeys.polygonBlockExplorer
            Chain.TEZOS -> throw Exception("Tezos is not supported")
            Chain.GHOSTNET -> throw Exception("Ghostnet is not supported")
        }
    }

    fun isEVMChain(chain: Chain): Boolean{
        val EVMChains= listOf(Chain.ETHEREUM, Chain.POLYGON, Chain.GOERLI, Chain.MUMBAI)
        if(chain in EVMChains) return true
        return false
    }

    fun isTezosChain(chain: Chain): Boolean{
        val TezosChains= listOf(Chain.TEZOS, Chain.GHOSTNET)
        if(chain in TezosChains) return true
        return false
    }
}

