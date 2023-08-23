package id.walt.nftkit.utilis.providers

import id.walt.nftkit.services.EVMChain

object ProviderFactory {

    fun getProvider(chain: EVMChain): Web3jInstance? = when (chain) {
        EVMChain.ETHEREUM -> EthereumWeb3()
        EVMChain.GOERLI -> GoerliWeb3()
        EVMChain.SEPOLIA -> SepoliaWeb3()
        EVMChain.POLYGON -> PolygonWeb3()
        EVMChain.MUMBAI -> MumbaiWeb3()
        EVMChain.ASTAR -> AstarWeb3()
        EVMChain.MOONBEAM -> MoonbeamWeb3()
        EVMChain.SHIMMEREVM -> IotaWeb3()
    }
}

