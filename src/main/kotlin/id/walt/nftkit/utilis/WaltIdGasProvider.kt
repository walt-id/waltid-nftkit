package id.walt.nftkit

import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger
// 1 MATIC => 1.000.000.000 Gwei
// 1 Gwei =>  1.000.000.000 Wei

// Gas (16th Mai 2022): 0.68$
// - https://cointool.app/gasPrice/matic
// - https://polygonscan.com/address/0xb789711a8cf2a3938779d01866a4c376598b9fe6

// Config Gas: 80 Wei
object WaltIdGasProvider : StaticGasProvider(BigInteger.valueOf(19_000_000_000L), BigInteger.valueOf(2_498_868))