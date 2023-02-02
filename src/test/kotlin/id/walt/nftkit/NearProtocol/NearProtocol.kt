package id.walt.nftkit.NearProtocol

import id.walt.nftkit.services.NearNftService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

class NearProtocol : StringSpec  ({
    var enableTest = true
    "create subaccount".config(enabled = false) {
        val result = NearNftService.createSubAccount("khaled_lightency1.testnet", "testingsub.khaled_lightency1.testnet", "4" , "testnet")

        result shouldNotBe null
    }

    "deploy smart contract to subaccount with default metadata".config(enabled = enableTest) {
        val result = NearNftService.deployContractDefault("testingsub.khaled_lightency1.testnet", "testnet")

        result shouldNotBe null
    }




})
