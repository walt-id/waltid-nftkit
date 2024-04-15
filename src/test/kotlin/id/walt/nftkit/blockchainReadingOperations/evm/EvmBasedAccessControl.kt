package id.walt.nftkit.blockchainReadingOperations.evm

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedAccessControl : StringSpec({

    "Verify smart contract ownership".config() {
        val result= AccessControlService.owner(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe  "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
    }

    "Testing the role".config() {
        val result = AccessControlService.hasRole(EVMChain.AMOY,"0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE", "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result shouldBe true
    }

     "Get admin of an account".config(){
         val result = AccessControlService.getRoleAdmin(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB","MINTER_ROLE")
         result shouldBe "00000000000000000000000000000000"
     }











})