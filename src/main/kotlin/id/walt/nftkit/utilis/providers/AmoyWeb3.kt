package id.walt.nftkit.utilis.providers

import id.walt.nftkit.services.WaltIdServices
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

class AmoyWeb3 : Web3jInstance {
    override fun getWeb3j(): Web3j {
        return Web3j.build(HttpService(WaltIdServices.loadChainConfig().providers.amoy))
    }
}