package id.walt.nftkit.chains.evm.erc721

import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import org.web3j.abi.datatypes.DynamicBytes;

interface ERC721TokenStandard {


    //fun DeployNewToken(tokenOwnerId: string?, TokenName: string?, TokenSymbol: string?): Task<TransactionReciept?>?


    fun mintToken(
        contractAddress: String,
        recipient: Address,
        tokenURI: Utf8String
    ): TransactionReceipt?


    fun ownerOf(contractAddress: String, tokenId: Uint256): String?

    fun name(contractAddress: String): String?

    fun symbol(contractAddress: String): String?

    fun tokenURI(contractAddress: String, tokenId: Uint256): String?

    fun balanceOf(contractAddress: String, owner: Address): BigInteger?


    /*fun transfer(
        contractAddress: String?,
        SenderId: string?,
        RecipientId: string?,
        TokenId: Long
    ): Task<TransactionReciept?>?*/

    /*fun transferFrom(
        contractAddress: String?,
        from: Address?,
        to: Address?,
        tokenId: Uint256
    ): RemoteFunctionCall<TransactionReceipt>?

    fun transferFrom(
        contractAddress: String?,
        from: Address?,
        to: Address?,
        tokenId: Uint256,
        data : DynamicBytes
    ): RemoteFunctionCall<TransactionReceipt>?*/

    //fun getApproved(contractAddress: String?, tokenId: Uint256): Address

    /*fun isApprovedForAll(
        contractAddress: String?,
        owner: Address?,
        operator: Address
    ): Bool?*/
    /*fun safeTransferFrom(
      contractAddress: String?,
      from: Address?,
      to: Address?,
      RecipientId: Uint256?,
      tokenId: Long
  ): RemoteFunctionCall<TransactionReceipt>?

  fun setApprovalForAll(
      contractAddress: String?,
      operator: Address?,
      approved: Bool?
  ): RemoteFunctionCall<TransactionReceipt>?*/

    /*fun approve(contractAddress: String?,
               to: Address,
               tokenId: Uint256
   ): RemoteFunctionCall<TransactionReceipt>?*/

    //fun Burn(ContractAddress: string?, CallerId: string?, TokenId: Long): Task<TransactionReciept?>?



}
