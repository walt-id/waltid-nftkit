package id.walt.nftkit.extensions

import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.ExtensionsService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedSCExtensions : StringSpec({

    val enableTest = false


    "Smart contract pause function ".config(enabled=false) {
        val result= ExtensionsService.pause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe true
        result.transactionId shouldNotBe null
    }

    "Smart contract paused state ".config(enabled=false) {
        val result= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe true
    }

    "Smart contract unpause function ".config(enabled=false) {
        val result= ExtensionsService.unpause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe false
        result.transactionId shouldNotBe null
    }
    "set transferable test".config(enabled=false){
        val oldV = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        val result = ExtensionsService.setTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldV )
        val newV = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.transactionId shouldNotBe null
        newV shouldNotBe oldV
    }

    "set burnable test".config(enabled=false) {
        val oldV = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val result = ExtensionsService.setBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldV)
        val newV = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result.transactionId shouldNotBe null
        newV shouldNotBe oldV
    }

  /*  "update token URI".config(enabled=false){
        val updatetokenuri = updateToken
        val result = ExtensionsService.setTokenURI(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "6" )

    } */
})