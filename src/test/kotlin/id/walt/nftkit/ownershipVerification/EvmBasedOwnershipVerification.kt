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
    val result = VerificationService.verifyTrait(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517","4", traitType="", traitValue = null )
    result shouldBe true
    }

    "data nft verification".config(enabled=enableTest){
        val result = VerificationService.dataNftVerification(Chain.MUMBAI,
            erc721FactorycontractAddress= "",
            erc721contractAddress= "",
            account="",
            propertyKey="",
            propertyValue="")
        result shouldBe true
    }


})