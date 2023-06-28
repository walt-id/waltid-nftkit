package id.walt.nftkit.rest.nftdata

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.rest.AccessControlController
import id.walt.nftkit.services.EVMChain
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object NFTDataController {
    fun getDetailData(ctx: Context) {

    }

    fun getListData(ctx: Context) {

    }

    fun getDetailDataDocs() = document().operation {
        it.summary("Retrieve nft detail data")
            .operationId("getDetailData").addTagsItem(AccessControlController.TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<NftDetailDataTransferObject>("200") { }

    fun getListDataDocs() = document().operation {
        it.summary("Retrieve nft detail data")
            .operationId("getDetailData").addTagsItem(AccessControlController.TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }
}