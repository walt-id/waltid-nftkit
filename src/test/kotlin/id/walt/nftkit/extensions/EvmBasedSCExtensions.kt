package id.walt.nftkit.extensions

import id.walt.nftkit.rest.UpdateTokenURIRequest
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.ExtensionsService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigInteger


class EvmBasedSCExtensions : StringSpec({

    val enableTest = false


    "Smart contract pause function ".config(enabled=enableTest) {
        val result= ExtensionsService.pause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe true
        result.transactionId shouldNotBe null
    }

    "Smart contract paused state ".config(enabled=enableTest) {
        val result= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe true
    }

    "Smart contract unpause function ".config(enabled=enableTest) {
        val result= ExtensionsService.unpause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe false
        result.transactionId shouldNotBe null
    }
    "set transferable test".config(enabled=enableTest){
        val oldT = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        val result = ExtensionsService.setTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldT )
        val newT = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.transactionId shouldNotBe null
        newT shouldNotBe oldT
    }

    "set burnable test".config(enabled=enableTest) {
        val oldB = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val result = ExtensionsService.setBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldB)
        val newB = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result.transactionId shouldNotBe null
        newB shouldNotBe oldB
    }
/*
     "update token URI".config(enabled=false){
       val metadata =
       val param =  UpdateTokenURIRequest("","")
       val result = ExtensionsService.setTokenURI(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "4" )
   }
*/
    "burn token test".config(enabled=enableTest){
        val tokenid = BigInteger.valueOf(4)
        val result = ExtensionsService.burn(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid)
        result.transactionId shouldNotBe null
    }


})