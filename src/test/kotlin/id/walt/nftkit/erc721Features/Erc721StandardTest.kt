package id.walt.nftkit.erc721Features

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

class Erc721StandardTest : StringSpec({

    val enableTest = false


    "Smart contract deployment".config(enabled = enableTest) {
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val result = NftService.deploySmartContractToken(Chain.MUMBAI, deploymentParameter, deploymentOptions)
        result.contractAddress shouldNotBe  ""
        result.contractAddress shouldNotBe null
    }


})