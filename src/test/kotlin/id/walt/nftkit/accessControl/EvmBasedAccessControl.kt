package id.walt.nftkit.accessControl

import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedAccessControl : StringSpec({

    val enableTest = false

    "Verify smart contract ownership" {
        val result= AccessControlService.owner(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe  "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
    }



})