package id.walt.nftkit.utilis

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

class BasicWeb3Instance : Web3jInstance {
    override fun getWeb3j(): Web3j {
        return Web3j.build(HttpService(""))
    }
}