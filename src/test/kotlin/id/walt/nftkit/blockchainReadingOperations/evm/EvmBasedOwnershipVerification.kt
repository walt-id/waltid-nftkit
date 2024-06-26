package id.walt.nftkit.blockchainReadingOperations.evm

import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger

class EvmBasedOwnershipVerification : StringSpec({


    "Verify NFT ownership".config() {
        val result= VerificationService.verifyNftOwnership(Chain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74", "1")
        result shouldBe  true
    }

    "verify trait type and trait value in the metadata".config(){
        val resTrue =  VerificationService.verifyNftOwnershipWithTraits(Chain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74","3", traitType="Trait 1", traitValue = "Value 1" )
        resTrue shouldBe true
    }

})