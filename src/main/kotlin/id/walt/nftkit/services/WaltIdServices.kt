package id.walt.nftkit.services

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.yaml.YamlParser
import id.walt.nftkit.Values
import java.io.File
import java.util.*


data class Providers(
    val ethereum: String,
    val goerli: String,
    val sepolia: String,
    val polygon: String,
    val mumbai: String,
    val astar: String,
    val moonbeam: String,
    val opal: String,
    val unique: String,
    val shimmerevm: String
)

data class ChainConfig(val providers: Providers, val privateKey: String)

data class KeysConfig(val keys: Map<String, String>)

data class ApiKeys(
    val ethereumBlockExplorer: String,
    val polygonBlockExplorer: String,
    val shimmerBlockExplorer: String,
    val alchemy: String,
    val nftstorage: String,
    val subscan: String
)

data class BlockExplorerScanApiKeyConfig(val apiKeys: ApiKeys)
data class TezosConfig(val tezosBackendServer: String)
data class NearConfig(val nearBackendServer: String)

data class PolkadotConfig(val polkadotAccounts: Map<String, String>)

data class IndexersUrl(val uniqueUrl: String, val opalUrl: String)
data class Indexers(val indexersUrl: IndexersUrl)

data class algorandSeedMnemonic(val algorand_seed_Mnemonic: String)
data class Algorand(val algorandConfig: algorandSeedMnemonic)


val WALTID_CONFIG_PATH = System.getenv("WALTID_CONFIG_PATH") ?: "."

object WaltIdServices {
    const val default_yaml_path = "/walt-default.yaml"
    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    fun decBase64Str(base64: String): String = String(Base64.getDecoder().decode(base64))

    val chainConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<ChainConfig>()
    }
    fun loadChainConfig() = chainConfig

    val apiKeysConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<BlockExplorerScanApiKeyConfig>()
    }
    fun loadApiKeys() = apiKeysConfig

    val accountKeyConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<KeysConfig>()
    }
    fun loadAccountKeysConfig() = accountKeyConfig

    val tezosConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<TezosConfig>()
    }
    fun loadTezosConfig() = tezosConfig

    val nearConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource("/walt-default.yaml"))
            .build()
            .loadConfigOrThrow<NearConfig>()
    }
    fun loadNearConfig() = nearConfig

    val polkadot by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource("/walt-default.yaml"))
            .build()
            .loadConfigOrThrow<PolkadotConfig>()
    }
    fun loadPolkadotConfig() = polkadot

    val indexersConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<Indexers>()
    }

    fun loadIndexers() = indexersConfig

    val algorandConfig by lazy {
        ConfigLoader.builder()
            .addFileExtensionMapping("yaml", YamlParser())
            .addSource(PropertySource.file(File("$WALTID_CONFIG_PATH/walt.yaml"), optional = true))
            .addSource(PropertySource.resource(default_yaml_path))
            .build()
            .loadConfigOrThrow<Algorand>()
    }

    fun loadAlgorand() = algorandConfig


    fun getBlockExplorerUrl(chain: EVMChain): String {
        return when (chain) {
            EVMChain.ETHEREUM -> Values.ETHEREUM_MAINNET_BLOCK_EXPLORER_URL
            EVMChain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_BLOCK_EXPLORER_URL
            EVMChain.SEPOLIA -> Values.ETHEREUM_TESTNET_SEPOLIA_BLOCK_EXPLORER_URL
            EVMChain.POLYGON -> Values.POLYGON_MAINNET_BLOCK_EXPLORER_URL
            EVMChain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_BLOCK_EXPLORER_URL
            EVMChain.SHIMMEREVM -> Values.SHIMMEREVM_TESTNET_BLOCK_EXPLORER_URL
            else -> {
                throw Exception("${chain.toString()} is not supported")
            }
        }
    }
}
