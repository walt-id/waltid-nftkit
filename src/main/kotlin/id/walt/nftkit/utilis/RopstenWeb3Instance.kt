package id.walt.nftkit.utilis

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

class RopstenWeb3Instance : Web3jInstance {
    override fun getWeb3j(): Web3j {
        return Web3j.build(HttpService("https://rinkeby.infura.io/v3/0184192d0f2942c3b0322d79eca162b2"))
    }
}