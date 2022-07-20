package id.walt.nftkit.accessControl

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedAccessControl : StringSpec({

    val enableTest = false

    "Verify smart contract ownership".config(enabled=enableTest) {
        val result= AccessControlService.owner(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe  "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
    }

    "Transfer ownership of smart contract".config(enabled=enableTest){
        val result = AccessControlService.transferOwnership(Chain.MUMBAI,
            "0x41ffba64969ab4d8a4ac7fae765e4fc1dd30290b",
            "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        )
        val nOwner= AccessControlService.owner(Chain.MUMBAI, "0x41ffba64969ab4d8a4ac7fae765e4fc1dd30290b")
        nOwner shouldBe "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        result.transactionId shouldNotBe null
    }

    "Renounce ownership of smart contract".config(enabled=enableTest){
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val SC = NftService.deploySmartContractToken(Chain.MUMBAI, deploymentParameter, deploymentOptions)
        val result = AccessControlService.renounceOwnership(Chain.MUMBAI, SC.contractAddress)
        val checkOwnership = AccessControlService.owner(Chain.MUMBAI, SC.contractAddress)

        result.transactionId shouldNotBe null
        checkOwnership shouldBe "0x0000000000000000000000000000000000000000"
    }

    "Granting role".config(enabled=enableTest){

        var hadRole = AccessControlService.hasRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        if (hadRole){
             val revoke = AccessControlService.revokeRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE", "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
             hadRole = AccessControlService.hasRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        }

        val result = AccessControlService.grantRole(Chain.MUMBAI,"0xa5a0914988bAB4e773109969A9176855eA77FcfB", role="MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        val checkGrantedRole = AccessControlService.hasRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result.transactionId shouldNotBe null
        hadRole shouldBe false
        checkGrantedRole shouldBe true
    }

    "Testing the role".config(enabled=enableTest) {
        val result = AccessControlService.hasRole(Chain.MUMBAI,"0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE", "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result shouldBe true
    }

    "Revoking role".config(enabled=enableTest){
        val result = AccessControlService.revokeRole(Chain.MUMBAI,"0xa5a0914988bAB4e773109969A9176855eA77FcfB","MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        val checkRole = AccessControlService.hasRole(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result.transactionId shouldNotBe null
        checkRole shouldBe false
    }


     "Get admin of an account".config(enabled=enableTest){
         val result = AccessControlService.getRoleAdmin(Chain.MUMBAI, "0xa5a0914988bAB4e773109969A9176855eA77FcfB","MINTER_ROLE")
         result shouldBe "00000000000000000000000000000000"
     }











})