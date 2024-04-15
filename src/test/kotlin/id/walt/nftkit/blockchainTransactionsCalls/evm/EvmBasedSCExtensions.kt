package id.walt.nftkit.blockchainTransactionsCalls.evm

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

    "Smart contract pause function ".config() {
        val result= ExtensionsService.pause(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe true
        result.transactionId shouldNotBe null
    }

    "Smart contract unpause function ".config() {
        val result= ExtensionsService.unpause(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe false
        result.transactionId shouldNotBe null
    }

    "Setting token URI".config(){
        val oldURI = NftService.getNftMetadataUri(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", BigInteger.valueOf(4) )
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val metadata = NftMetadata(
            description= "ticket 1 description updated in $currentDate",
            external_url= "xx",
            image= "xx",
            image_data= "xx",
            name= "ticket 1" ,
            attributes = null
        )
        val param =  UpdateTokenURIRequest("",metadata)
        val result = ExtensionsService.setTokenURI(EVMChain.AMOY,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "4",signedAccount = null ,parameter = param )
        val newURI = NftService.getNftMetadataUri(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", BigInteger.valueOf(4))
        newURI shouldNotBe oldURI
        result.transactionId shouldNotBe null
    }

    "Changing transferable status".config(){
        val oldT = ExtensionsService.getTransferable(EVMChain.AMOY,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        val result = ExtensionsService.setTransferable(EVMChain.AMOY,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldT )
        val newT = ExtensionsService.getTransferable(EVMChain.AMOY,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.transactionId shouldNotBe null
        newT shouldBe !oldT
    }

    "Changing burnable status".config() {
        val oldB = ExtensionsService.getBurnable(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val result = ExtensionsService.setBurnable(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldB)
        val newB = ExtensionsService.getBurnable(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result.transactionId shouldNotBe null
        newB shouldBe !oldB
    }

    "Burning token".config(){
        val nftMetaData = NftMetadata("To burn", "To burn", "string", "string","string")
        val mintingParametre = MintingParameter(metadataUri = "", recipientAddress="0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31", metadata = nftMetaData )
        val mintingOption = MintingOptions(MetadataStorageType.ON_CHAIN)
        val newNftId = NftService.mintToken(EVMChain.AMOY, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",mintingParametre, mintingOption ,false).tokenId
        if (!(ExtensionsService.getBurnable(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")))
        {ExtensionsService.setBurnable(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", true) }
            val result = newNftId?.let {
                ExtensionsService.burn(
                    EVMChain.AMOY,
                    "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
                    it
                )
            }
        if (result != null) {
            shouldThrow<Exception> { NftService.getNftMetadataUri(EVMChain.AMOY, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", newNftId) }
            result.transactionId shouldNotBe null
        }
        }

})