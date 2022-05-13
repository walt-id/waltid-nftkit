package id.walt.nftkit

import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object WaltIdGasProvider : StaticGasProvider(BigInteger.valueOf(60_000_000_000L), BigInteger.valueOf(4_498_868)) {

}