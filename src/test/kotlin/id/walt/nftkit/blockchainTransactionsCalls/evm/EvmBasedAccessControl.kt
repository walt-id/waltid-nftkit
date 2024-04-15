package id.walt.nftkit.blockchainTransactionsCalls.evm

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EvmBasedAccessControl : StringSpec({

    "Transfer ownership of smart contract".config(){
        val result = AccessControlService.transferOwnership(EVMChain.AMOY,
            "0x41ffba64969ab4d8a4ac7fae765e4fc1dd30290b",
            "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        )
        val nOwner= AccessControlService.owner(EVMChain.AMOY, "0x41ffba64969ab4d8a4ac7fae765e4fc1dd30290b")
        nOwner shouldBe "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31"
        result.transactionId shouldNotBe null
    }

    "Renounce ownership of smart contract".config(){
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV", DeploymentParameter.Options(true, true))
        val SC = NftService.deploySmartContractToken(EVMChain.AMOY, deploymentParameter, deploymentOptions)
        val result = AccessControlService.renounceOwnership(EVMChain.AMOY, SC.contractAddress)
        val checkOwnership = AccessControlService.owner(EVMChain.AMOY, SC.contractAddress)

        result.transactionId shouldNotBe null
        checkOwnership shouldBe "0x0000000000000000000000000000000000000000"
    }

    "Granting role".config(){

        var hadRole = AccessControlService.hasRole(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        if (hadRole){
             val revoke = AccessControlService.revokeRole(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE", "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
             hadRole = AccessControlService.hasRole(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        }

        val result = AccessControlService.grantRole(EVMChain.AMOY,"0xa5a0914988bAB4e773109969A9176855eA77FcfB", role="MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        val checkGrantedRole = AccessControlService.hasRole(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result.transactionId shouldNotBe null
        hadRole shouldBe false
        checkGrantedRole shouldBe true
    }


    "Revoking role".config(){
        val result = AccessControlService.revokeRole(EVMChain.AMOY,"0xa5a0914988bAB4e773109969A9176855eA77FcfB","MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31" )
        val checkRole = AccessControlService.hasRole(EVMChain.AMOY, "0xa5a0914988bAB4e773109969A9176855eA77FcfB", "MINTER_ROLE" , "0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31")
        result.transactionId shouldNotBe null
        checkRole shouldBe false
    }

})