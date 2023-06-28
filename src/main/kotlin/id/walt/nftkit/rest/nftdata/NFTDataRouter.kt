package id.walt.nftkit.rest.nftdata

import io.javalin.apibuilder.ApiBuilder
import io.javalin.plugin.openapi.dsl.documented

object NFTDataRouter {
    fun routes(parentPath: String) {
        ApiBuilder.path("$parentPath/data") {
            ApiBuilder.get("list", documented(NFTDataController.getListDataDocs(), NFTDataController::getListData))
            ApiBuilder.get("detail", documented(NFTDataController.getDetailDataDocs(), NFTDataController::getDetailData))
        }
    }
}