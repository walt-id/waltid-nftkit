package id.walt.nftkit.services

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.hikari.HikariDataSourceDecoder
import com.sksamuel.hoplite.yaml.YamlParser
import java.io.File
import java.util.*


data class Providers(val ethereum: String, val rinkeby: String, val ropsten: String, val polygon:String)
data class providersConfig(val providers: Providers)

object WaltIdServices {

    const val etherScan =""

    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    fun decBase64Str(base64: String): String = String(Base64.getDecoder().decode(base64))

    fun loadProvidersConfig() = ConfigLoader.Builder()
        .addFileExtensionMapping("yaml", YamlParser())
        .addSource(PropertySource.file(File("walt.yaml"), optional = true))
        .addSource(PropertySource.resource("/walt-default.yaml"))
        .addDecoder(HikariDataSourceDecoder())
        .build()
        .loadConfigOrThrow<providersConfig>()
}