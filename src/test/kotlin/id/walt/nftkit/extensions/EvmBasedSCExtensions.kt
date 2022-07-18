package id.walt.nftkit.extensions

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

    val enableTest = false


    "Smart contract pause function ".config(enabled=enableTest) {
        val result= ExtensionsService.pause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe true
        result.transactionId shouldNotBe null
    }

    "Smart contract paused state ".config(enabled=enableTest) {
        val result= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result shouldBe true
    }

    "Smart contract unpause function ".config(enabled=enableTest) {
        val result= ExtensionsService.unpause(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val pausedValue= ExtensionsService.paused(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        pausedValue shouldBe false
        result.transactionId shouldNotBe null
    }
    "changing transferable status".config(enabled=enableTest){
        val oldT = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        val result = ExtensionsService.setTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldT )
        val newT = ExtensionsService.getTransferable(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.transactionId shouldNotBe null
        newT shouldBe !oldT
    }


     "update token URI".config(enabled=enableTest){
         val oldURI = NftService.getNftMetadataUri(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", BigInteger.valueOf(4) )

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
         val result = ExtensionsService.setTokenURI(Chain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", "4",signedAccount = null ,parameter = param )
         val newURI = NftService.getNftMetadataUri(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", BigInteger.valueOf(4))

         newURI shouldNotBe oldURI
         result.transactionId shouldNotBe null
     }


    "changing burnable status".config(enabled=enableTest) {
        val oldB = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        val result = ExtensionsService.setBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", !oldB)
        val newB = ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")
        result.transactionId shouldNotBe null
        newB shouldBe !oldB
    }

    "burning token".config(enabled=enableTest){
        //Creating new nft
        val nftMetaData = NftMetadata("To burn", "To burn", "string", "string","string")
        val mintingParametre = MintingParameter(metadataUri = "", recipientAddress="0xaf87c5ce7a1fb6bd5aadb6dd9c0b8ef51ef1bc31", metadata = nftMetaData )
        val mintingOption = MintingOptions(MetadataStorageType.ON_CHAIN)
        val newNftId = NftService.mintToken(Chain.MUMBAI, contractAddress = "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",mintingParametre, mintingOption).tokenId

        if (!(ExtensionsService.getBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517")))
        {ExtensionsService.setBurnable(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", true) }
            val result = newNftId?.let {
                ExtensionsService.burn(
                    Chain.MUMBAI,
                    "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
                    it
                )
            }

        if (result != null) {
            shouldThrow<Exception> { NftService.getNftMetadataUri(Chain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", newNftId) }
            result.transactionId shouldNotBe null
        }
        }

})