package id.walt.nftkit.NearProtocol

import id.walt.nftkit.services.NearNftService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

class NearProtocol : StringSpec  ({
    var enableTest = true
    "create subaccount".config(enabled = enableTest) {
        val result = NearNftService.createSubAccount("khaled_lightency1.testnet", "testingsub.khaled_lightency1.testnet", "4" , "testnet")

        result shouldNotBe null
    }




})
