package id.walt.nftkit.services

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.yaml.YamlParser
import id.walt.nftkit.Values
import java.io.File
import java.util.*


data class Providers(val ethereum: String, val rinkeby: String, val ropsten: String, val polygon: String, val mumbai: String)
data class ChainConfig(val providers: Providers, val privateKey: String)

data class KeysConfig(val keys: Map<String, String>)

data class ApiKeys(val ethereumBlockExplorer: String, val polygonBlockExplorer: String, val alchemy: String, val nftstorage: String)
data class BlockExplorerScanApiKeyConfig(val apiKeys: ApiKeys)
data class TezosConfig(val tezosBackendServer: String)

val WALTID_CONFIG_PATH = System.getenv("WALTID_CONFIG_PATH") ?: "."

object WaltIdServices {

    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    fun decBase64Str(base64: String): String = String(Base64.getDecoder().decode(base64))

    fun loadChainConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<ChainConfig>()

    fun loadApiKeys() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<BlockExplorerScanApiKeyConfig>()

    fun loadAccountKeysConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<KeysConfig>()

    fun loadTezosConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<TezosConfig>()


    fun getBlockExplorerUrl(chain: Chain): String {
        return when (chain) {
            Chain.ETHEREUM -> Values.ETHEREUM_MAINNET_BLOCK_EXPLORER_URL
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_BLOCK_EXPLORER_URL
            Chain.ROPSTEN -> Values.ETHEREUM_TESTNET_ROPSTEN_BLOCK_EXPLORER_URL
            Chain.POLYGON -> Values.POLYGON_MAINNET_BLOCK_EXPLORER_URL
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_BLOCK_EXPLORER_URL
            Chain.TEZOS -> throw Exception("Tezos is not supported")
            Chain.GHOSTNET -> throw Exception("Ghostnet is not supported")
        }
    }
}
