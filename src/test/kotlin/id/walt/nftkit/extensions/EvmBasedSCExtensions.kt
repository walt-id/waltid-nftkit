package id.walt.nftkit.extensions

import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.ExtensionsService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EvmBasedSCExtensions : StringSpec({

    val enableTest = false

    "Smart contract paused state " {
        val result= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe false
    }


})