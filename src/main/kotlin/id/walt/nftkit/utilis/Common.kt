package id.walt.nftkit.utilis

import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.MetadataStorageType
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.services.WaltIdServices
import org.web3j.protocol.core.methods.response.TransactionReceipt

object Common {

    fun getTransactionResponse(chain: Chain, transactionReceipt: TransactionReceipt): TransactionResponse {
        val url = WaltIdServices.getBlockExplorerUrl(chain)
        return TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt!!.transactionHash}")
    }

    fun getMetadataType(uri: String): MetadataStorageType {
        if(uri.contains("data:application/json;base64", true)){
            return MetadataStorageType.ON_CHAIN
        }else{
            return MetadataStorageType.OFF_CHAIN
        }
    }

    inline fun <reified T : Enum<T>> getEnumValue(strVal: String) = strVal.let {
        if (it.isEmpty()) {
            throw Exception("No ${T::class.java.name} defined")
        }
        enumValueOf<T>(it.uppercase())
    }
}

