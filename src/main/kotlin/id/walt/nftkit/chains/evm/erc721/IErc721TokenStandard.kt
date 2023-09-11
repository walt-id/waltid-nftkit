package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.services.EVMChain
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger

interface IErc721TokenStandard {


    //fun DeployNewToken(tokenOwnerId: string?, TokenName: string?, TokenSymbol: string?): Task<TransactionReciept?>?


    fun mintToken(
        chain: EVMChain,
        contractAddress: String,
        recipient: Address,
        tokenURI: Utf8String
    ): TransactionReceipt?


    fun ownerOf(chain: EVMChain, contractAddress: String, tokenId: Uint256): String?

    fun name(chain: EVMChain, contractAddress: String): String?

    fun symbol(chain: EVMChain, contractAddress: String): String?

    fun tokenURI(chain: EVMChain, contractAddress: String, tokenId: Uint256): String?

    fun balanceOf(chain: EVMChain, contractAddress: String, owner: Address): BigInteger?

    fun supportsInterface(chain: EVMChain, contractAddress: String): Boolean

    fun updateTokenUri(chain: EVMChain, contractAddress: String, token: BigInteger, tokenURI: Utf8String, signedAccount: String?) : TransactionReceipt?

    fun transferFrom(chain: EVMChain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, signedAccount: String?): TransactionReceipt
    fun safeTransferFrom(chain: EVMChain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, signedAccount: String?): TransactionReceipt
    fun safeTransferFrom(chain: EVMChain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, data: DynamicBytes, signedAccount: String?): TransactionReceipt
    fun setApprovalForAll(chain: EVMChain, contractAddress: String, operator: Address, approved: Bool, signedAccount: String?): TransactionReceipt
    fun isApprovedForAll(chain: EVMChain, contractAddress: String, owner: Address, operator: Address): Bool
    fun approve(chain: EVMChain, contractAddress: String, to: Address, tokenId: Uint256, signedAccount: String?):TransactionReceipt
    fun getApproved(chain: EVMChain, contractAddress: String, tokenId: Uint256): Address


}
