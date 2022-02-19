package id.walt.nftkit

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    println(App().greeting)

    val web3j = Web3j.build(HttpService("<your_node_url>"))

}
