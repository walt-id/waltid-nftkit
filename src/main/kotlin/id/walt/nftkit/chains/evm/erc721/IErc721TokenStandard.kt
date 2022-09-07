package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.services.Chain
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
        chain: Chain,
        contractAddress: String,
        recipient: Address,
        tokenURI: Utf8String
    ): TransactionReceipt?


    fun ownerOf(chain: Chain, contractAddress: String, tokenId: Uint256): String?

    fun name(chain: Chain, contractAddress: String): String?

    fun symbol(chain: Chain, contractAddress: String): String?

    fun tokenURI(chain: Chain, contractAddress: String, tokenId: Uint256): String?

    fun balanceOf(chain: Chain, contractAddress: String, owner: Address): BigInteger?

    fun supportsInterface(chain: Chain, contractAddress: String): Boolean

    fun updateTokenUri(chain: Chain, contractAddress: String, token: BigInteger, tokenURI: Utf8String, signedAccount: String?) : TransactionReceipt?

    fun transferFrom(chain: Chain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, signedAccount: String?): TransactionReceipt
    fun safeTransferFrom(chain: Chain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, signedAccount: String?): TransactionReceipt
    fun safeTransferFrom(chain: Chain, contractAddress: String, from: Address, to: Address, tokenId: Uint256, data: DynamicBytes, signedAccount: String?): TransactionReceipt
    fun setApprovalForAll(chain: Chain, contractAddress: String, operator: Address, approved: Bool, signedAccount: String?): TransactionReceipt
    fun isApprovedForAll(chain: Chain, contractAddress: String, owner: Address, operator: Address): Bool
    fun approve(chain: Chain, contractAddress: String, to: Address, tokenId: Uint256, signedAccount: String?):TransactionReceipt
    fun getApproved(chain: Chain, contractAddress: String, tokenId: Uint256): Address


}
