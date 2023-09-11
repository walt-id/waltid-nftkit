package id.walt.nftkit.blockchainReadingOperations.tezos

import id.walt.nftkit.opa.DynamicPolicyArg
import id.walt.nftkit.opa.PolicyRegistry
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TezosNftsOwnershipVerification : StringSpec({

    "NFT ownership verification".config() {
        val result = VerificationService.verifyNftOwnership(
            Chain.TEZOS,
            "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi",
            "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu",
            "1462880"
        )
        result shouldBe false
    }

    "NFT ownership verification within a collection ".config() {
        val result = VerificationService.verifyNftOwnershipWithinCollection(
            Chain.TEZOS,
            "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi",
            "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu"
        )
        result shouldBe false
    }

    "NFT ownership verification with traits".config() {
        val result = VerificationService.verifyNftOwnershipWithTraits(
            Chain.TEZOS,
            "KT1L7GvUxZH5tfa6cgZKnH6vpp2uVxnFVHKu",
            "tz1eBKt2zTv69sniuVNbPsW2RwuC28d9R2VB",
            "3691",
            "Background",
            "Yellow"
        )
        result shouldBe true
    }

    "Verify an NFT metadata against a dynamic policy".config(enabled = false) {
        val policy = "package app.nft\n" +
                "\n" +
                "import future.keywords.if\n" +
                "\n" +
                "default allow := false\n" +
                "\n" +
                "\n" +
                "allow if {\n" +
                "\tvalid_nft_Background\n" +
                "\tvalid_nft_Body\n" +
                "}\n" +
                "\n" +
                "\n" +
                "valid_nft_Background if input.Background= data.Background\n" +
                "\n" +
                "valid_nft_Body if input.Body= data.Body"
        val input = mutableMapOf<String, String?>()
        input.put("Background", "Purple")
        input.put("Body", "Body 11")
        val policyName = "policy 1"
        val dynArg = DynamicPolicyArg(policyName, "policy 1", input, policy, "data.app.nft.allow")
        PolicyRegistry.createSavedPolicy(dynArg.name, dynArg)
        val chain = Chain.TEZOS
        val contractAddress = "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi"
        val tokenId = "1462880"
        val result = VerificationService.verifyPolicy(chain, contractAddress, tokenId, policyName)
        result shouldBe true
    }
})
