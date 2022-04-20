package id.walt.nftkit

import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object WaltIdGasProvider : StaticGasProvider(BigInteger.valueOf(25000000000L), BigInteger.valueOf(2500000)) {

}