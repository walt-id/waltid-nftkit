package id.walt.nftkit.rest


import cc.vileda.openapi.dsl.components
import cc.vileda.openapi.dsl.externalDocs
import cc.vileda.openapi.dsl.info
import cc.vileda.openapi.dsl.securityScheme
import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import id.walt.nftkit.Values
import id.walt.nftkit.rest.RootController.healthDocs
import id.walt.rest.OpenAPIUtils.documentedIgnored
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.json.JsonMapper
import io.javalin.plugin.openapi.InitialConfigurationCreator
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.dsl.documented
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import mu.KotlinLogging

object NftKitApi {

    private val log = KotlinLogging.logger {}

    internal const val DEFAULT_CORE_API_PORT = 7000

    internal const val DEFAULT_BIND_ADDRESS = "0.0.0.0"

    /**
     * Currently used instance of the NFT Kit API server
     */
    var api: Javalin? = null

    /**
     * Start NFT KIT REST API
     * @param apiTargetUrls (optional): add URLs to Swagger documentation for easy testing
     * @param bindAddress (default: 127.0.0.1): select address to bind on to, e.g. 0.0.0.0 for all interfaces
     * @param port (default: 7000): select port to listen on
     */
    fun start(
        port: Int = DEFAULT_CORE_API_PORT,
        bindAddress: String = DEFAULT_BIND_ADDRESS,
        apiTargetUrls: List<String> = listOf()
    ) {
        log.info { "Starting NFT KIT API ...\n" }

        api = Javalin.create {
            it.apply {
                registerPlugin(RouteOverviewPlugin("/api-routes"))

                registerPlugin(OpenApiPlugin(OpenApiOptions(InitialConfigurationCreator {
                    OpenAPI().apply {
                        info {
                            title = "walt.id NFT Kit API"
                            description = "The walt.id public API documentation"
                            contact = Contact().apply {
                                name = "walt.id"
                                url = "https://walt.id"
                                email = "office@walt.id"
                            }
                            version = Values.version
                        }
                        servers = listOf(
                            Server().url("/"),
                            *apiTargetUrls.map { Server().url(it) }.toTypedArray()
                        )
                        externalDocs {
                            description = "walt.id docs"
                            url = "https://docs.walt.id"
                        }

                        components {
                            securityScheme {
                                name = "bearerAuth"
                                type = SecurityScheme.Type.HTTP
                                scheme = "bearer"
                                `in` = SecurityScheme.In.HEADER
                                description = "HTTP Bearer Token authentication"
                                bearerFormat = "JWT"
                            }
                        }
                    }
                }).apply {
                    path("/nftkit/api-documentation")
                    swagger(SwaggerOptions("/nftkit/swagger").title("walt.id NFT Kit API"))
                    reDoc(ReDocOptions("/nftkit/redoc").title("walt.id NFT Kit API"))
                }))

                val mapper: ObjectMapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .findAndAddModules()
                    .build()

                this.jsonMapper(object : JsonMapper {
                    override fun toJsonString(obj: Any): String {
                        return Klaxon().toJsonString(obj)
                    }

                    override fun <T : Any?> fromJsonString(json: String, targetClass: Class<T>): T {
                        return JavalinJackson(mapper).fromJsonString(json, targetClass)
                    }
                })

                //addStaticFiles("/static")
            }

            it.enableCorsForAllOrigins()

            it.enableDevLogging()

            it.maxRequestSize = 30_000_000L
        }.routes {
            get("", documented(documentedIgnored(), RootController::root))
            get("health", documented(healthDocs(), RootController::health))
            path("nftkit") {
                path("nft") {
                    post("chain/{chain}/contract/deploy", documented(NftController.deployDocs(), NftController::deploy))
                    
                    post(
                        "chain/{chain}/contract/{contractAddress}/token/mint",
                        documented(NftController.mintDocs(), NftController::mint)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}/metadataUri",
                        documented(NftController.getNftMetadatUriDocs(), NftController::getNftMetadatUri)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}/metadata",
                        documented(NftController.getNftMetadataDocs(), NftController::getNftMetadata)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/owner/{ownerAddress}/balance",
                        documented(NftController.balanceDocs(), NftController::balance)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}/owner",
                        documented(NftController.ownerDocs(), NftController::owner)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/info",
                        documented(NftController.tokenCollectionInfoDocs(), NftController::tokenCollectionInfo)
                    )
                    get(
                        "chain/{chain}/owner/{ownerAddress}",
                        documented(NftController.getAccountNFTsDocs(), NftController::getAccountNFTs)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}/metadata",
                        documented(NftController.updateMetadataDocs(), NftController::updateMetadata)
                    )
                    post(
                        "ipfs/file/Upload",
                        documented(NftController.uploadFileToIpfsDocs(), NftController::uploadFileToIpfs)
                    )

                }
                path("nft/verifier") {
                    post(
                        "chain/{chain}/contract/{contractAddress}/verifyCollection",
                        documented(VerificationController.verifyCollectionDocs(), VerificationController::verifyCollection)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/verifyTrait",
                        documented(
                            VerificationController.verifyCollectionWithTraitsDocs(),
                            VerificationController::verifyCollectionWithTraits
                        )
                    )
                    post(
                        "oceanDao/chain/{chain}/contract/{contractAddress}/verification",
                        documented(
                            VerificationController.oceanDaoVerificationDocs(),
                            VerificationController::oceanDaoVerification
                        )
                    )
                }
                path("nft/extensions") {
                    get(
                        "chain/{chain}/contract/{contractAddress}/paused",
                        documented(ExtensionsController.pausedDocs(), ExtensionsController::paused)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/pause",
                        documented(ExtensionsController.pauseDocs(), ExtensionsController::pause)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/unpause",
                        documented(ExtensionsController.unpauseDocs(), ExtensionsController::unpause)
                    )
                    put(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}/tokenURI",
                        documented(ExtensionsController.updateTokenURIDocs(), ExtensionsController::updateTokenURI)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/transferable",
                        documented(ExtensionsController.getTransferableDocs(), ExtensionsController::getTransferable)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/transferable",
                        documented(ExtensionsController.setTransferableDocs(), ExtensionsController::setTransferable)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/burnable",
                        documented(ExtensionsController.getBurnableDocs(), ExtensionsController::getBurnable)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/burnable",
                        documented(ExtensionsController.setBurnableDocs(), ExtensionsController::setBurnable)
                    )
                    delete(
                        "chain/{chain}/contract/{contractAddress}/token/{tokenId}",
                        documented(ExtensionsController.burnDocs(), ExtensionsController::burn)
                    )

                }
                path("nft/accessControl") {
                    get(
                        "chain/{chain}/contract/{contractAddress}/owner",
                        documented(AccessControlController.ownerDocs(), AccessControlController::owner)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/ownershipTransfer",
                        documented(AccessControlController.transferOwnershipDocs(), AccessControlController::transferOwnership)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/ownershipRenounce",
                        documented(AccessControlController.renounceOwnershipDocs(), AccessControlController::renounceOwnership)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/account/{account}/role/{role}",
                        documented(AccessControlController.hasRoleDocs(), AccessControlController::hasRole)
                    )
                    get(
                        "chain/{chain}/contract/{contractAddress}/role/{role}/admin",
                        documented(AccessControlController.roleAdminDocs(), AccessControlController::roleAdmin)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/grantrole",
                        documented(AccessControlController.grantRoleDocs(), AccessControlController::grantRole)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/revokeRole",
                        documented(AccessControlController.revokeRoleDocs(), AccessControlController::revokeRole)
                    )
                    post(
                        "chain/{chain}/contract/{contractAddress}/renounceRole",
                        documented(AccessControlController.renounceRoleDocs(), AccessControlController::renounceRole)
                    )

                }
            }

        }.exception(InvalidFormatException::class.java) { e, ctx ->
            log.error(e.stackTraceToString())
            ctx.json(ErrorResponse(e.message ?: " Unknown application error", 400))
            ctx.status(400)
        }.exception(IllegalArgumentException::class.java) { e, ctx ->
            log.error(e.stackTraceToString())
            ctx.json(ErrorResponse(e.message ?: " Unknown application error", 400))
            ctx.status(400)
        }.exception(Exception::class.java) { e, ctx ->
            log.error(e.stackTraceToString())
            ctx.json(ErrorResponse(e.message ?: " Unknown server error", 500))
            ctx.status(500)
        }.start(bindAddress, port)
    }

    /**
     * Stop NFT Kit API if it's currently running
     */
    fun stop() = api?.stop()
}

fun main() {
    NftKitApi.start()
}
