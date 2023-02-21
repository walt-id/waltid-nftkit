package id.walt.nftkit.blockchainTransactionsCalls.near

import id.walt.nftkit.services.NearChain
import id.walt.nftkit.services.NearNftService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should

class NearProtocol : StringSpec  ({
    val random_subaccount = "test" + System.currentTimeMillis()
//
//    "create sub account".config(enableTest) {
//        val result = NearNftService.createSubAccount("khaled_lightency1.testnet", "${random_subaccount}.khaled_lightency1.testnet", "4" , "testnet")
//
//        result.valid()
//    }
//
//    "deploy smart contract to subaccount with default metadata".config(enableTest) {
//        val result = NearNftService.deployContractDefault("${random_subaccount}.khaled_lightency1.testnet", "testnet")
//
//        result shouldNotBe null
//    }
//
//    "mint nft".config(enableTest) {
//        val result = NearNftService.mintNftToken("${random_subaccount}.khaled_lightency1.testnet", "${random_subaccount}.khaled_lightency1.testnet" , "token1", "testnet token",
//            "token for testing" , "https://images.squarespace-cdn.com/content/v1/609c0ddf94bcc0278a7cbdb4/e73d850a-aad6-419e-84fd-f549ee10ad30/Walt_Logo_Small.png", "", "" , "" , "khaled_lightency1.testnet", "testnet")
//
//        result shouldNotBe null
//    }

    "Get NFT for owner".config() {
        val result = NearNftService.getNFTforAccount("khaled_lightency1.testnet","nft.khaled_lightency1.testnet", NearChain.testnet)
        println(result)
        result should { it != null }
    }

    "Get NFT per token_id".config() {
        val result = NearNftService.getTokenById("nft.khaled_lightency1.testnet","token1", NearChain.testnet)
        println(result)
        result should { it != null }
    }


})
