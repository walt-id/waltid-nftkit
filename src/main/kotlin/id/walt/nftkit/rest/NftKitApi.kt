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

    const val apiTitle = "walt.id NFT Kit API"
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
                            title = apiTitle
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
                    path("/v2/nftkit/api-documentation")
                    swagger(SwaggerOptions("/v2/nftkit/swagger").title(apiTitle))
                    reDoc(ReDocOptions("/v2/nftkit/redoc").title(apiTitle))
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
            path("v2") {
                path("nftkit") {
                    path("nft") {
                        post(
                            "chain/{chain}/contract/deploy",
                            documented(NftController.deployDocs(), NftController::deploy)
                        )
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
                        post(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/transferfrom",
                            documented(NftController.transferFromDocs(), NftController::transferFrom)
                        )
                        post(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/safetransferfrom",
                            documented(NftController.safeTransferFromDocs(), NftController::safeTransferFrom)
                        )
                        post(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/safefransferFromwithdata",
                            documented(NftController.safeTransferFromWithDataDocs(), NftController::safeTransferFromWithData)
                        )
                        post(
                            "chain/{chain}/contract/{contractAddress}/setapprovalforall",
                            documented(NftController.setApprovalForAllDocs(), NftController::setApprovalForAll)
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/isapprovedforall",
                            documented(NftController.isApprovedForAllDocs(), NftController::isApprovedForAll)
                        )
                        post(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/approve",
                            documented(NftController.approveDocs(), NftController::approve)
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/getapproved",
                            documented(NftController.getApprovedDocs(), NftController::getApproved)
                        )
                        path("tezos"){
                            post(
                                "chain/{chain}/contract/deploy",
                                documented(TezosNftController.deployDocs(), TezosNftController::deploy)
                            )
                            post(
                                "chain/{chain}/contract/{contractAddress}/mint",
                                documented(TezosNftController.mintDocs(), TezosNftController::mint)
                            )
                            post(
                                "chain/{chain}/contract/{contractAddress}/minter",
                                documented(TezosNftController.addMinterDocs(), TezosNftController::addMinter)
                            )
                            get(
                                "chain/{chain}/contract/{contractAddress}/token/{tokenId}/metadata",
                                documented(TezosNftController.getNftMetadataDocs(), TezosNftController::getNftMetadata)
                            )
                            get(
                                "chain/{chain}/contract/{contractAddress}/metadata",
                                documented(TezosNftController.getContractMetadataDocs(), TezosNftController::getContractMetadata)
                            )
                        }
                        path("near"){
                            post("chain/{chain}/account/create",
                                documented(NearNftController.createSubAccountDocs(), NearNftController::createSubAccount)
                            )
                            post("/chain/{chain}/contract/account/{account_id}/deploy/default",
                                documented(NearNftController.deployDefaultContractDocs(), NearNftController::deployDefaultContract)
                            )
                            post("/chain/{chain}/account/{account_id}/deploy",
                                documented(NearNftController.deployCustomContractDocs(), NearNftController::deployCustomContract)
                            )
                            post(
                                "/chain/{chain}/contract/{contract_id}/mint",
                                documented(NearNftController.mintDocs(), NearNftController::mint)
                            )
                            get("chain/{chain}/contract/{contract_id}/account/{account_id}/NFTS",
                                documented(NearNftController.getNftTokenDocs(), NearNftController::getNftToken)
                            )
                            get("chain/{chain}/contract/{contract_id}/NFT/{token_id}",
                                documented(NearNftController.getNFTTokenMetadataDocs(), NearNftController::getNFTTokenMetadata)
                            )
                            get("/chain/{chain}/contract/{contract_id}/NFT/metadata",
                                documented(NearNftController.getNFTContractMetadataDocs(), NearNftController::getNFTContractMetadata)
                            )

                        }

                        path("flow"){
                            post("chain/{chain}/account/{account_id}/AllNFTs",
                                documented(FlowNftController.getAllNFTsDocs() , FlowNftController::getAllNFTs)
                            )
                            post("chain/{chain}/account/{account_id}/{contractAddress}/{collectionPublicPath}/{token_id}/getNFTById",
                                documented(FlowNftController.getNFTbyIdDocs() , FlowNftController::getNFTbyId)
                            )
                            post("chain/{chain}/account/{account_id}/{collectionPath}/getNFTinCollection",
                                documented(FlowNftController.getNFTinCollectionDocs() , FlowNftController::getNFTinCollection)
                            )
                                 }

                        path("unique") {
                            get(
                                "chain/{network}/account/{account}/",
                                documented(
                                    PolkadotUniqueNftController.fetchUniqueNftsDocs(),
                                    PolkadotUniqueNftController::fetchUniqueNfts)
                            )
                            get("chain/{chain}/collection/{collectionId}/token/{tokenId}/metadata",
                                documented(
                                    PolkadotUniqueNftController.fetchUniqueNftMetadataDocs(),
                                    PolkadotUniqueNftController::fetchUniqueNftMetadata)
                            )
                        }
                        path("parachain") {
                            get("{chain}/account/{account}/subscan",
                                documented(
                                    PolkadotParaChainNftController.fetchParachainNFTsDocs(),
                                    PolkadotParaChainNftController::fetchParachainNFTs)
                            )
                            get("{chain}/account/{account}/EvmErc721/subscan",
                                documented(
                                    PolkadotParaChainNftController.fetchEvmErc721CollectiblesBySubscanDocs(),
                                    PolkadotParaChainNftController::fetchEvmErc721CollectiblesBySubscan)

                            )
                        }

                        path("Algorand"){

                            post("account/create/",
                                documented(AlgorandNftController.accountCreationDocs(), AlgorandNftController::accountCreation)
                            )
                            post("asset/create/{assetName}/{assetUnitName}/{url}/",
                                documented(AlgorandNftController.assetCreationDocs(), AlgorandNftController::assetCreation)
                            )

                            get(
                                "chain/{chain}/asset/{assetId}",
                                documented(AlgorandNftController.fetchTokenDocs(), AlgorandNftController::fetchToken)
                            )
                            get(
                                "chain/{chain}/asset/{assetId}/params",
                                documented(AlgorandNftController.fetchAssetMetadataDocs(), AlgorandNftController::fetchAssetMetadata)
                            )
                            get(
                                "chain/{chain}/asset/{assetId}/metadata",
                                documented(AlgorandNftController.fetchNftMetadataDocs(), AlgorandNftController::fetchNftMetadata)
                            )
                            get(
                                "chain/{chain}/assets/account/{address}/",
                                documented(AlgorandNftController.fetchAccountAssetsDocs(), AlgorandNftController::fetchAccountAssets)
                            )

                        }
                    }
                    path("nft/verifier") {
                        get(
                            "chain/{chain}/verifyNftOwnership",
                            documented(VerificationController.verifyAlgorandNftOwnershipDocs(), VerificationController::verifyAlgorandNftOwnership)
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/verifyNftOwnership",
                            documented(VerificationController.verifyNftOwnershipDocs(), VerificationController::verifyNftOwnership)
                        )
                        get(
                            "chain/{chain}/collection/{collectionId}/verifyNftOwnership",
                            documented(VerificationController.verifyNftOwnershipWithCollectionIdDocs(), VerificationController::verifyNftOwnershipWithCollectionId)
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/verifyNftOwnershipWithinCollection",
                            documented(VerificationController.verifyNftOwnershipWithinCollectionDocs(), VerificationController::verifyNftOwnershipWithinCollection)
                        )
                        get(
                            "chain/{chain}/collection/{collectionId}/verifyNftOwnershipWithinCollection",
                            documented(VerificationController.verifyNftOwnershipWithinCollectionWithCollectionIdDocs(), VerificationController::verifyNftOwnershipWithinCollectionWithCollectionId)
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/verifyNftOwnershipWithTraits",
                            documented(
                                VerificationController.verifyNftOwnershipWithTraitsDocs(),
                                VerificationController::verifyNftOwnershipWithTraits
                            )
                        )
                        get(
                            "chain/{chain}/collection/{collectionId}/verifyNftOwnershipWithTraits",
                            documented(
                                VerificationController.verifyNftOwnershipWithTraitsWithCollectionIdDocs(),
                                VerificationController::verifyNftOwnershipWithTraitsWithCollectionId
                            )
                        )
                        post(
                            "policies/create",
                            documented(
                                VerificationController.createDynamicPolicyDocs(),
                                VerificationController::createDynamicPolicy
                            )
                        )
                        get(
                            "policies",
                            documented(
                                VerificationController.listPoliciesDocs(),
                                VerificationController::listPolicies
                            )
                        )
                        get(
                            "chain/{chain}/contract/{contractAddress}/token/{tokenId}/policy/{policyName}/verification",
                            documented(
                                VerificationController.verifyNftPolicyDocs(),
                                VerificationController::verifyNftPolicy
                            )
                        )
                        get(
                            "chain/{chain}/collection/{collectionId}/token/{tokenId}/policy/{policyName}/verification",
                            documented(
                                VerificationController.verifyNftPolicyWithCollectionIdDocs(),
                                VerificationController::verifyNftPolicyWithCollectionId
                            )
                        )
                        get(
                            "oceanDao/chain/{chain}/contract/{contractAddress}/verification",
                            documented(
                                VerificationController.oceanDaoVerificationDocs(),
                                VerificationController::oceanDaoVerification
                            )
                        )

                        path("flow"){

                                get(
                                    "chain/{chain}/contract/{contractAddress}/verifyNftOwnership/Flow",
                                    documented(VerificationController.verifyNftOwnershipOnFlowDocs(), VerificationController::verifyNftOwnershipOnFlow)
                                )
                                get(
                                    "chain/{chain}/contract/{contractAddress}/verifyNftOwnershipWithinCollection/Flow",
                                    documented(VerificationController.verifyNftOwnershipInCollectionOnFlowDocs(), VerificationController::verifyNftOwnershipInCollectionOnFlow)
                                )
                                get(
                                    "chain/{chain}/contract/{contractAddress}/collectionPath/{collectionPath}/token/{tokenId}/policy/{policyName}/verification",
                                    documented(
                                        VerificationController.verifyNftPolicyFlowDocs(),
                                        VerificationController::verifyNftPolicyFlow
                                    )
                                )


                        }
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
