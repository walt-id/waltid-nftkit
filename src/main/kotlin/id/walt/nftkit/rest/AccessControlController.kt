package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.EVMChain
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

data class GrantRevokeRoleRequest(val role: String, val account: String)
data class OwneTransferrRequest(val account: String)




object AccessControlController {
    val TAG1 ="EVM based Blockchain"

    fun owner(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.owner(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun ownerDocs() = document().operation {
        it.summary("Ownable: Owner")
            .operationId("owner").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }

    fun transferOwnership(ctx: Context) {
        val transferOwnerReq = ctx.bodyAsClass(OwneTransferrRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.transferOwnership(
            Common.getEVMChain(chain.uppercase()),
            contractAddress,
            transferOwnerReq.account
        )
        ctx.json(
            result
        )
    }

    fun transferOwnershipDocs() = document().operation {
        it.summary("Ownable: Transfer ownership")
            .operationId("TransferOwnership").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<OwneTransferrRequest> {
        it.description("")
    }.json<Boolean>("200") { }

    fun renounceOwnership(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.renounceOwnership(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun renounceOwnershipDocs() = document().operation {
        it.summary("Ownable: Renounce ownership")
            .operationId("RenounceOwnership").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }


    fun hasRole(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.pathParam("account")
        val role = ctx.pathParam("role")
        val result = AccessControlService.hasRole(Common.getEVMChain(chain.uppercase()), contractAddress, role, account)
        ctx.json(
            result
        )
    }

    fun hasRoleDocs() = document().operation {
        it.summary("Role based access control: Has Role")
            .operationId("hasRole").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }

    fun roleAdmin(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val role = ctx.pathParam("role")
        val result = AccessControlService.getRoleAdmin(Common.getEVMChain(chain.uppercase()), contractAddress, role)
        ctx.json(
            result
        )
    }

    fun roleAdminDocs() = document().operation {
        it.summary("Role based access control: Role admin")
            .operationId("roleAdmin").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.pathParam<String>("role") {
    }.json<TransactionResponse>("200") { }

    fun grantRole(ctx: Context) {
        val grantRevokeRoleReq = ctx.bodyAsClass(GrantRevokeRoleRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.grantRole(
            Common.getEVMChain(chain.uppercase()),
            contractAddress,
            grantRevokeRoleReq.role,
            grantRevokeRoleReq.account
        )
        ctx.json(
            result
        )
    }

    fun grantRoleDocs() = document().operation {
        it.summary("Role based access control: Grant role")
            .operationId("grantRole").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<GrantRevokeRoleRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }


    fun revokeRole(ctx: Context) {
        val grantRevokeRoleReq = ctx.bodyAsClass(GrantRevokeRoleRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.revokeRole(
            Common.getEVMChain(chain.uppercase()),
            contractAddress,
            grantRevokeRoleReq.role,
            grantRevokeRoleReq.account
        )
        ctx.json(
            result
        )
    }

    fun revokeRoleDocs() = document().operation {
        it.summary("Role based access control: Revoke role")
            .operationId("revokeRole").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<GrantRevokeRoleRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }

    fun renounceRole(ctx: Context) {
        val grantRevokeRoleReq = ctx.bodyAsClass(GrantRevokeRoleRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = AccessControlService.renounceRole(
            Common.getEVMChain(chain.uppercase()),
            contractAddress,
            grantRevokeRoleReq.role,
            grantRevokeRoleReq.account
        )
        ctx.json(
            result
        )
    }

    fun renounceRoleDocs() = document().operation {
        it.summary("Role based access control: Renounce role")
            .operationId("renounceRole").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<GrantRevokeRoleRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }


}
