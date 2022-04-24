package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.rest.UpdateTokenURIRequest
import id.walt.nftkit.utilis.Common
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import smart_contract_wrapper.CustomAccessControlERC721
import java.math.BigInteger

object ExtensionsService {

    fun paused(chain: Chain, contractAddress: String): Boolean {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        return customAccessControlERC721Wrapper.paused().send().value
    }

    fun pause(chain: Chain, contractAddress: String): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = customAccessControlERC721Wrapper.pause().send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun unpause(chain: Chain, contractAddress: String): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = customAccessControlERC721Wrapper.unpause().send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun setTokenURI(
        chain: Chain,
        contractAddress: String,
        tokenId: String,
        parameter: UpdateTokenURIRequest
    ): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        var tokenUri: String?;
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri()
            tokenUri = metadataUri.getTokenUri(parameter.metadata)
        }
        val transactionReceipt =
            customAccessControlERC721Wrapper.setTokenURI(Uint256(BigInteger(tokenId)), Utf8String(tokenUri)).send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun getTransferable(chain: Chain, contractAddress: String): Boolean {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        return customAccessControlERC721Wrapper.tokensTransferable().send().value
    }

    fun setTransferable(chain: Chain, contractAddress: String, transferable: Boolean): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = customAccessControlERC721Wrapper.setTransferableOption(Bool(transferable)).send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun getBurnable(chain: Chain, contractAddress: String): Boolean? {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        return customAccessControlERC721Wrapper.tokensBurnable().send().value
    }


    fun setBurnable(chain: Chain, contractAddress: String, Burnable: Boolean): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = customAccessControlERC721Wrapper.setBurnableOption(Bool(Burnable)).send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun burn(chain: Chain, contractAddress: String, tokenId: BigInteger): TransactionResponse {
        val customAccessControlERC721Wrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = customAccessControlERC721Wrapper.burn(Uint256(tokenId)).send()
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    private fun loadOwnableContract(chain: Chain, address: String): CustomAccessControlERC721 {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider

        if (chain == Chain.POLYGON || chain == Chain.MUMBAI) {
            val chainId: Long
            if (chain == Chain.POLYGON) {
                chainId = Values.POLYGON_MAINNET_CHAIN_ID
            } else {
                chainId = Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            }
            val transactionManager: TransactionManager = RawTransactionManager(
                web3j, credentials, chainId
            )
            return CustomAccessControlERC721.load(address, web3j, transactionManager, gasProvider)
        } else {
            return CustomAccessControlERC721.load(address, web3j, credentials, gasProvider)
        }
    }
}
