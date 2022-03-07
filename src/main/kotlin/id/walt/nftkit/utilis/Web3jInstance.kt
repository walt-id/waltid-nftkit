package id.walt.nftkit.utilis

import org.web3j.protocol.Web3j

interface Web3jInstance {
    fun getWeb3j(): Web3j
}