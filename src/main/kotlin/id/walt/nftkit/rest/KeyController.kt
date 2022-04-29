package id.walt.nftkit.rest


import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable

@Serializable
data class GenKeyRequest(
    val keyAlgorithm: String,
)

@Serializable
data class ExportKeyRequest(
    val keyAlias: String,
//    val format: KeyFormat = KeyFormat.JWK,
    val exportPrivate: Boolean = false
)

object KeyController {

//    private val keyService = KeyService.getService()

    fun gen(ctx: Context) {
        val genKeyReq = ctx.bodyAsClass(GenKeyRequest::class.java)
//        ctx.json(keyService.generate(genKeyReq.keyAlgorithm))
        ctx.json("")
    }

    fun genDocs() = document().operation {
        it.summary("Generate key")
            .operationId("genKey").addTagsItem("Key Management")
    }.body<GenKeyRequest> {
        it.description("The desired key algorithm (ECDSA_Secp256k1 or EdDSA_Ed25519)")
    }.json<String>("200") { it.description("Key ID") }

    fun load(ctx: Context) {
        ctx.json(
            ""
        )
    }

    fun loadDocs() = document().operation {
        it.summary("Load public key").addTagsItem("Key Management")
    }.json<String>("200")

    fun delete(ctx: Context) {
        println(ctx.body())
        // ctx.json(keyService.delete(ctx.body()))
        ctx.json("")
    }

    fun deleteDocs() = document().operation {
        it.summary("Delete key").operationId("deleteKey").addTagsItem("Key Management")

    }.body<String> {
        it.description("ID of key to be deleted")
    }.json<String>("200")

    fun export(ctx: Context) {
        val req = ctx.bodyAsClass(ExportKeyRequest::class.java)
//        ctx.result(
//            keyService.export(
//                req.keyAlias,
//                req.format,
//                null
//            )
//        )
        ctx.result("")
    }

    fun exportDocs() = document().operation {
        it.summary("Exports public and private key part (if supported by underlying keystore)").operationId("exportKey")
            .addTagsItem("Key Management")
    }.body<ExportKeyRequest> { it.description("Exports the key in JWK or PEM format") }
        .json<String>("200") { it.description("The key in the desired formant") }

    fun list(ctx: Context) {
        val keyIds = ArrayList<String>()
//        keyService.listKeys().forEach { (keyId) -> keyIds.add(keyId.id) }
        ctx.json(keyIds)
    }

    fun listDocs() = document().operation {
        it.summary("List of key IDs").operationId("listKeys").addTagsItem("Key Management")
    }.json<Array<String>>("200") { it.description("The desired key IDs") }

    fun import(ctx: Context) {
        // val req = ctx.bodyAsClass(ImportKeyRequest::class.java)
        // ctx.json(keyService.importKey(ctx.body()))
        ctx.json("")
    }

    fun importDocs() = document().operation {
        it.summary("Import key").operationId("importKey").addTagsItem("Key Management")
    }.body<String> {
        it.description("Imports the key (JWK format) to the key store")
    }.json<String>("200")
}
