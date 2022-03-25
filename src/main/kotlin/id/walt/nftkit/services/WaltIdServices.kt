package id.walt.nftkit.services

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.hikari.HikariDataSourceDecoder
import com.sksamuel.hoplite.yaml.YamlParser
import id.walt.nftkit.Values
import java.io.File
import java.util.*


data class Providers(val ethereum: String, val rinkeby: String, val ropsten: String, val polygon:String, val mumbai: String)
data class ChainConfig(val providers: Providers, val privateKey: String)

data class BlockExplorerScanApiKeys(val ethereum: String, val polygon:String)
data class BlockExplorerScanApiKeyConfig(val blockExplorerScanApiKeys: BlockExplorerScanApiKeys)

object WaltIdServices {

    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    fun decBase64Str(base64: String): String = String(Base64.getDecoder().decode(base64))

    fun loadChainConfig()= ConfigLoader.Builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<ChainConfig>()

    fun loadChainScanApiKeys()= ConfigLoader.Builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<BlockExplorerScanApiKeyConfig>()


    fun getBlockExplorerUrl(chain: Chain): String {
        return when(chain){
            Chain.ETHEREUM -> Values.ETHEREUM_MAINNET_BLOCK_EXPLORER_URL
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_BLOCK_EXPLORER_URL
            Chain.ROPSTEN -> Values.ETHEREUM_TESTNET_ROPSTEN_BLOCK_EXPLORER_URL
            Chain.POLYGON -> Values.POLYGON_MAINNET_BLOCK_EXPLORER_URL
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_BLOCK_EXPLORER_URL
        }
    }
}