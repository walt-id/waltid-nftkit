package id.walt.nftkit.blockchainReadingOperations.evm

import id.walt.nftkit.services.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.web3j.crypto.Credentials
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class Erc721Standard : StringSpec({

    "Verifying nft metadata".config(){
        val tokenid = BigInteger.valueOf(3)
        val result = NftService.getNftMetadata(EVMChain.MUMBAI, "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )
        result.name shouldBe "Ticket #1"
        result.description shouldBe "Ticket #1 Description"

    }

    "Verifying Metadata URI".config(){
        val tokenid = BigInteger.valueOf(10)
        val result = NftService.getNftMetadataUri(EVMChain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517", tokenid )
        result shouldBe "ipfs://bafyreiebsxbmwmgrzlrhlpwj2mmz5j64vsfojfy7lviu22unkkmjdhfqt4/metadata.json"
    }

    "Verifying balance of address".config(){
        val result = NftService.balanceOf(EVMChain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
            "0xe895D59e84d0E77a8DaEaA55547528406C5a1314")
        result shouldBe BigInteger.valueOf(2)
    }

    "Verifying owner of a token".config(){
        val tokenid = BigInteger.valueOf(35)
        val result = NftService.ownerOf(EVMChain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517",
        tokenid)
        result shouldBe "0xe895d59e84d0e77a8daeaa55547528406c5a1314"
    }

    "Verifying token collection info".config(){
        val result = NftService.getTokenCollectionInfo(EVMChain.MUMBAI,
            "0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" )
        result.name shouldBe "Ticket"
        result.symbol shouldBe "TK"
    }

    //TODO: Fix https://docs.alchemy.com/reference/getnfts
//    "Verifying account NFTs by Alchmy".config(){
//        val result = NftService.getAccountNFTsByAlchemy(Chain.MUMBAI,
//            "0xe895D59e84d0E77a8DaEaA55547528406C5a1314" )
//        result[0].id.tokenId shouldBe "35"
//        result[1].id.tokenId shouldBe "36"
//    }


    "Verifying IPFS metadata using NFTs storage".config(){
        val tokenid = BigInteger.valueOf(10)
        var uri = NftService.getNftMetadataUri(EVMChain.MUMBAI,"0xf277BE034881eE38A9b270E5b6C5c6f333Af2517" ,tokenid)
        val result = NftService.getIPFSMetadataUsingNFTStorage(uri)
        result.description shouldBe "string"
        result.name shouldBe "string"
        result.image shouldBe "string"
        result.external_url shouldBe "string"
        result.attributes?.get(0)!!.trait_type shouldBe "string"
        (result.attributes?.get(0)!!.value?.content ?:  "") shouldBe "15/7/2022 10:30:07"
    }


})