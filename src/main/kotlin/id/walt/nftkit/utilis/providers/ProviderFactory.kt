package id.walt.nftkit.utilis.providers

import id.walt.nftkit.services.Chain

object ProviderFactory {

    fun getProvider(chain: Chain): Web3jInstance? = when (chain) {
        Chain.ETHEREUM -> EthereumWeb3()
        Chain.GOERLI -> GoerliWeb3()
        Chain.POLYGON -> PolygonWeb3()
        Chain.MUMBAI -> MumbaiWeb3()
        else -> null
    }
}

