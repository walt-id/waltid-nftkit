package id.walt.nftkit.utilis

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

class RopstenWeb3Instance : Web3jInstance {
    override fun getWeb3j(): Web3j {
        return Web3j.build(HttpService("https://ropsten.infura.io/v3/868f60c9c4d149fe9dbd1c0acd378ae4"))
    }
}