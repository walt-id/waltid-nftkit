package id.walt.nftkit

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase

class DummyTest : StringSpec({

    val enableTest = false


    "my first test" {
        println("my test")
    }

    "my disabled test".config(enabled = enableTest) {
        println("my test")
    }

}){
    override suspend fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)

        println("before")

    }
}