package id.walt.nftkit

import id.walt.nftkit.chains.evm.erc721.Erc721OnchainCredential
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    println(App().greeting)

    val erc721OnchainCredential = Erc721OnchainCredential()

    val name = erc721OnchainCredential.name("0xc831de165bD2356230e60DF549324034dB5A3BD5")
    println(name)

    val tokenuri = Utf8String("ewogICJkZXNjcmlwdGlvbiI6ICJGcmllbmRseSBPcGVuU2VhIENyZWF0dXJlIHRoYXQgZW5qb3lzIGxvbmcgc3dpbXMgaW4gdGhlIG9jZWFuLiIsIAogICJleHRlcm5hbF91cmwiOiAiaHR0cHM6Ly9vcGVuc2VhY3JlYXR1cmVzLmlvLzMiLCAKICAiaW1hZ2UiOiAiaHR0cHM6Ly9zdG9yYWdlLmdvb2dsZWFwaXMuY29tL29wZW5zZWEtcHJvZC5hcHBzcG90LmNvbS9wdWZmcy8zLnBuZyIsIAogICJuYW1lIjogIkRhdmUgU3RhcmJlbGx5Igp9")
    val address : Address = Address("0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74")
    val receipt = erc721OnchainCredential.mintToken("0xc831de165bD2356230e60DF549324034dB5A3BD5", address,tokenuri)
    println(receipt)



    val tokenUri =  erc721OnchainCredential.tokenURI("0xc831de165bD2356230e60DF549324034dB5A3BD5", Uint256(2))
    println(tokenUri)


}
