package id.walt.nftkit.ownershipVerification

import id.walt.nftkit.opa.DynamicPolicyArg
import id.walt.nftkit.opa.PolicyRegistry
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import id.walt.nftkit.utilis.Common
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TezosNftsOwnershipVerification : StringSpec({
    val enableTest = false

    "NFT ownership verification".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnership(Chain.TEZOS, "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi", "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu", "1462880")
        result shouldBe  true
    }

    "NFT ownership verification within a collection ".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnershipWithinCollection(Chain.TEZOS, "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi", "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu")
        result shouldBe  true
    }

    "NFT ownership verification with traits".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnershipWithTraits(Chain.TEZOS, "KT1L7GvUxZH5tfa6cgZKnH6vpp2uVxnFVHKu", "tz1eBKt2zTv69sniuVNbPsW2RwuC28d9R2VB", "3691","Background","Yellow")
        result shouldBe  true
    }

    "Verify an NFT metadata against a dynamic policy".config(enabled=enableTest) {
        val policy="package app.nft\n" +
                "\n" +
                "import future.keywords.if\n" +
                "\n" +
                "default allow := false\n" +
                "\n" +
                "\n" +
                "allow if {\n" +
                "\tvalid_datanft_Backgrounds\n" +
                "\tvalid_datanft_symbol\n" +
                "}\n" +
                "\n" +
                "\n" +
                "valid_datanft_Backgrounds if input.Backgrounds= data.Backgrounds\n" +
                "\n" +
                "valid_datanft_symbol if input.symbol= data.symbol"
        val input = mutableMapOf<String, String?>()
        input.put("Backgrounds","Green")
        input.put("symbol","RMB")
        input.put("reference","R1")
        val policyName= "policy1"
        val dynArg= DynamicPolicyArg(policyName, "policy1", input, policy, "data.app.nft.allow")
        PolicyRegistry.createSavedPolicy(dynArg.name, dynArg)
        val chain= Chain.GHOSTNET
        val contractAddress= "KT1Ennr99qgqzKEUCEqypXEexH4wWzVL5a9m"
        val tokenId="0"
        val result= VerificationService.verifyPolicy(chain, contractAddress, tokenId, policyName)
        result shouldBe  true
    }
})
