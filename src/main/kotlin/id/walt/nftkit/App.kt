package id.walt.nftkit

import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    println(App().greeting)

    val web3j = Web3j.build(HttpService("https://ropsten.infura.io/v3/0184192d0f2942c3b0322d79eca162b2"))//0xc831de165bD2356230e60DF549324034dB5A3BD5
    val credentials: Credentials = Credentials.create("d720ef2cb49c6cbe94175ed413d27e635c5acaa1b7cf03d1faad3a0abc2f53f3")
    println("Account address: " + credentials.address);
    val gasProvider: ContractGasProvider = WaltIdGasProvider
    val credentialNFT : CredentialNFT = CredentialNFT.load("0xc831de165bD2356230e60DF549324034dB5A3BD5", web3j, credentials, gasProvider)
    val address : Address = Address("0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74")
    System.out.println(credentialNFT.balanceOf(address).send().value)
    val tokenuri = Utf8String("test")
    val receipt: TransactionReceipt = credentialNFT.mintTo(address, tokenuri).send()
    println(receipt.transactionHash)
    println(credentialNFT.balanceOf(address).send().value)
    val tokenuriValue = credentialNFT.tokenURI(Uint256(BigInteger.valueOf(2))).send().value
    println(tokenuriValue)

}
