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

    "deploy smart contract to subaccount with default metadata".config(enabled = false) {
        val result = NearNftService.deployContractDefault("testingsub.khaled_lightency1.testnet", "testnet")

        result shouldNotBe null
    }

    "mint nft".config(enabled = true) {
        val result = NearNftService.mintNftToken("testingsub.khaled_lightency1.testnet", "testingsub.khaled_lightency1.testnet" , "token1", "testnet token",
            "token for testing" , "https://images.squarespace-cdn.com/content/v1/609c0ddf94bcc0278a7cbdb4/e73d850a-aad6-419e-84fd-f549ee10ad30/Walt_Logo_Small.png", "", "" , "" , "khaled_lightency1.testnet", "testnet")

        result shouldNotBe null
    }





})
