package id.walt.nftkit.ownershipVerification

import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TezosNftsOwnershipVerification : StringSpec({
    val enableTest = false

    "NFT ownership verification".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnership(Chain.TEZOS, "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi", "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu", "1462880")
        result shouldBe  true
    }

    "NFT ownership verification".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnershipWithinCollection(Chain.TEZOS, "KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi", "tz2AbAdd7KXzJMxHZUKoRwEDfL9j2peBKgyu")
        result shouldBe  true
    }

    "NFT ownership verification".config(enabled=enableTest) {
        val result= VerificationService.verifyNftOwnershipWithTraits(Chain.TEZOS, "KT1L7GvUxZH5tfa6cgZKnH6vpp2uVxnFVHKu", "tz1eBKt2zTv69sniuVNbPsW2RwuC28d9R2VB", "3691","Background","Yellow")
        result shouldBe  true
    }
})
