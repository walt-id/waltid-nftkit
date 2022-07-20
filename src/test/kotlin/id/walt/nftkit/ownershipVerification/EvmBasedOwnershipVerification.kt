package id.walt.nftkit.ownershipVerification

import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger

class EvmBasedOwnershipVerification : StringSpec({

    val enableTest = false

    "Verify NFT ownership".config(enabled=enableTest) {
        val result= VerificationService.verifyCollection(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74", "1")
        result shouldBe  true
    }

    "verify trait type and trait value in the metadata".config(enabled = enableTest){
        val resTrue =  VerificationService.verifyTrait(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74","3", traitType="Trait 1", traitValue = "Value 1" )
        resTrue shouldBe true
    }

})