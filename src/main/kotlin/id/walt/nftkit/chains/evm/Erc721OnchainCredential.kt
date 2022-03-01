package id.walt.nftkit.chains.evm

import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import id.walt.nftkit.utilis.BasicWeb3Instance
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger

class Erc721OnchainCredential : ERC721TokenStandard {


    override fun mintToken(contractAddress: String, recipient: Address, tokenURI: Utf8String): TransactionReceipt? {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.mintTo(recipient, tokenURI).send()
    }

    override fun ownerOf(contractAddress: String, tokenId: Uint256): String? {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.ownerOf(tokenId).send().value
    }

    override fun name(contractAddress: String): String {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.name().send().value
    }

    override fun symbol(contractAddress: String): String {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.symbol().send().value
    }

    override fun tokenURI(contractAddress: String, tokenId: Uint256): String? {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.tokenURI(tokenId).send().value
    }

    override fun balanceOf(contractAddress: String, owner: Address): BigInteger? {
        val erc721OnchainCredentialWrapper = loadContract(contractAddress)
        return erc721OnchainCredentialWrapper.balanceOf(owner).send().value
    }

    private fun loadContract(address: String) : Erc721OnchainCredentialWrapper{
        val basicWeb3Instance : BasicWeb3Instance = BasicWeb3Instance()
        val web3j = basicWeb3Instance.getWeb3j()

        val credentials: Credentials = Credentials.create("")

        val gasProvider: ContractGasProvider = WaltIdGasProvider

        return Erc721OnchainCredentialWrapper.load(address, web3j, credentials, gasProvider)

    }
}