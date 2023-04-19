package id.walt.nftkit.services

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.yaml.YamlParser
import id.walt.nftkit.Values
import java.io.File
import java.util.*


data class Providers(val ethereum: String, val goerli: String, val polygon: String, val mumbai: String, val astar: String, val moonbeam: String, val opal: String, val unique: String)
data class ChainConfig(val providers: Providers, val privateKey: String)

data class KeysConfig(val keys: Map<String, String>)

data class ApiKeys(val ethereumBlockExplorer: String, val polygonBlockExplorer: String, val alchemy: String, val nftstorage: String, val subscan: String)
data class BlockExplorerScanApiKeyConfig(val apiKeys: ApiKeys)
data class TezosConfig(val tezosBackendServer: String)
data class NearConfig(val nearBackendServer: String)

data class PolkadotConfig(val polkadotAccounts: Map<String, String>)
data class Indexers(val unique: String, val opal: String)




val WALTID_CONFIG_PATH = System.getenv("WALTID_CONFIG_PATH") ?: "."

object WaltIdServices {
    const val default_yaml_path= "/walt-default.yaml"
    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    fun decBase64Str(base64: String): String = String(Base64.getDecoder().decode(base64))

    fun loadChainConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource(default_yaml_path))
        .build()
        .loadConfigOrThrow<ChainConfig>()

    fun loadApiKeys() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource(default_yaml_path))
        .build()
        .loadConfigOrThrow<BlockExplorerScanApiKeyConfig>()

    fun loadAccountKeysConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource(default_yaml_path))
        .build()
        .loadConfigOrThrow<KeysConfig>()

    fun loadTezosConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource(default_yaml_path))
        .build()
        .loadConfigOrThrow<TezosConfig>()

    fun loadNearConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<NearConfig>()

    fun loadPolkadotConfig() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .build()
        .loadConfigOrThrow<PolkadotConfig>()

    fun loadIndexers() = ConfigLoader.builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
        .addSource(PropertySource.resource(default_yaml_path))
        .build()
        .loadConfigOrThrow<Indexers>()


    fun getBlockExplorerUrl(chain: EVMChain): String {
        return when (chain) {
            EVMChain.ETHEREUM -> Values.ETHEREUM_MAINNET_BLOCK_EXPLORER_URL
            EVMChain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_BLOCK_EXPLORER_URL
            EVMChain.POLYGON -> Values.POLYGON_MAINNET_BLOCK_EXPLORER_URL
            EVMChain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_BLOCK_EXPLORER_URL
            else -> {throw Exception("${chain.toString()} is not supported")}
        }
    }
}
