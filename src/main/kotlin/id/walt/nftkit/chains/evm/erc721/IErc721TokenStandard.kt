package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.services.Chain
import org.web3j.abi.datatypes.Address
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
