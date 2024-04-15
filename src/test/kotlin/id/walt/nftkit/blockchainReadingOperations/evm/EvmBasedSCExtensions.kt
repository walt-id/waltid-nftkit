package id.walt.nftkit.blockchainReadingOperations.evm

import id.walt.nftkit.rest.UpdateTokenURIRequest
import id.walt.nftkit.services.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*


class EvmBasedSCExtensions : StringSpec({


    "Verifying Smart contract state ".config() {
        val result= ExtensionsService.paused(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe false
    }

})