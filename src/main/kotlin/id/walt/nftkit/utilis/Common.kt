package id.walt.nftkit.utilis

import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.services.WaltIdServices
import org.web3j.protocol.core.methods.response.TransactionReceipt

object Common {

     fun getTransactionResponse(chain: Chain, transactionReceipt: TransactionReceipt): TransactionResponse {
        val url = WaltIdServices.getBlockExplorerUrl(chain)
        return TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt!!.transactionHash}" )
    }

    fun getChain(chain: String): Chain{
        return chain.let {
            if (it.isEmpty()){
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }
    }
}