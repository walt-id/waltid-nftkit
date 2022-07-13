package id.walt.nftkit.accessControl

import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedAccessControl : StringSpec({

    val enableTest = false

    "Verify smart contract ownership".config(enabled=false) {
        val result= AccessControlService.owner(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe  "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
    }

    "transfer ownership".config(enabled=false){
        val result = AccessControlService.transferOwnership(Chain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        )
        val nOwner= AccessControlService.owner(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        nOwner shouldBe "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        result.transactionId shouldNotBe null
    }

    " grant role ".config(enabled=false){
        val result = AccessControlService.grantRole(Chain.MUMBAI,"0xa5a0914988bAB4e773109969A9176855eA77FcfB", role="MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        val has = AccessControlService.hasRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result.transactionId shouldNotBe null
        has shouldBe true;
    }

    "has role test".config(enabled=false) {
        val result = AccessControlService.hasRole(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "MINTER_ROLE", "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result shouldBe true
    }

    "revoke role".config(enabled=false){
        val result = AccessControlService.revokeRole(Chain.MUMBAI,"0xa5a0914988bAB4e773109969A9176855eA77FcfB","MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        result.transactionId shouldNotBe null
    }

    "get role admin test".config(enabled=false){
        val result = AccessControlService.getRoleAdmin(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517","DEFAULT_ADMIN_ROLE")

    }









})