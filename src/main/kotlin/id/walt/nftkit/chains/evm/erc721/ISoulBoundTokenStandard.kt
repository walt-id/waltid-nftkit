package id.walt.nftkit.chains.evm.erc721
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.EVMChain
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
interface ISoulBoundTokenStandard {

    fun safeMint(chain: EVMChain, contractAddress: String,to: String, uri: String): TransactionReceipt?
    fun ownerOf(chain: EVMChain, contractAddress: String, tokenId: Uint256):String?

    fun name(chain: EVMChain, contractAddress: String): String?

    fun symbol(chain: EVMChain, contractAddress: String): String?

    fun tokenURI(chain: EVMChain, contractAddress: String, tokenId: Uint256): String?

    fun balanceOf(chain: EVMChain, contractAddress: String, owner: Address): BigInteger?

    fun safeTransferFrom(chain: EVMChain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, signedAccount: String?): TransactionReceipt

    fun supportsInterface(chain: EVMChain, contractAddress: String): Boolean

    fun revoke(chain: EVMChain, contractAddress: String, tokenId: Uint256, signedAccount: String?): TransactionReceipt

    fun unequip(chain: EVMChain, contractAddress: String, tokenId: Uint256, signedAccount: String?): TransactionReceipt
}