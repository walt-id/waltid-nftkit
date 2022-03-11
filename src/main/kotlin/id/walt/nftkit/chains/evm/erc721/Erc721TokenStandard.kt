package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.DeploymentResponse
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.smart_contract_wrapper.ERC721URIStorage
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import id.walt.nftkit.utilis.RopstenWeb3Instance
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes4
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger


object Erc721TokenStandard : IErc721TokenStandard {


    fun deployContract(chain: Chain, name: String, symbol: String): DeploymentResponse {
        val basicWeb3Instance  = RopstenWeb3Instance()
        val web3j = basicWeb3Instance.getWeb3j()
        val credentials: Credentials = Credentials.create("d720ef2cb49c6cbe94175ed413d27e635c5acaa1b7cf03d1faad3a0abc2f53f3")
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val contract= ERC721URIStorage.deploy(web3j,credentials,gasProvider,Utf8String(name),Utf8String(symbol)).send()
        val ts = TransactionResponse(contract.transactionReceipt.get().transactionHash,"https://rinkeby.etherscan.io/tx/"+ contract.transactionReceipt.get().transactionHash)
        return DeploymentResponse(ts, contract.contractAddress, "https://rinkeby.etherscan.io/token/"+ contract.contractAddress)

    }
    override fun mintToken(chain: Chain, contractAddress: String, recipient: Address, tokenURI: Utf8String): TransactionReceipt? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.mintTo(recipient, tokenURI).send()
    }

    override fun ownerOf(chain: Chain, contractAddress: String, tokenId: Uint256): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.ownerOf(tokenId).send().value
    }

    override fun name(chain: Chain, contractAddress: String): String {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.name().send().value
    }

    override fun symbol(chain: Chain, contractAddress: String): String {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.symbol().send().value
    }

    override fun tokenURI(chain: Chain, contractAddress: String, tokenId: Uint256): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.tokenURI(tokenId).send().value
    }

    override fun balanceOf(chain: Chain, contractAddress: String, owner: Address): BigInteger? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.balanceOf(owner).send().value
    }

    override fun supportsInterface(chain: Chain, contractAddress: String) : Boolean {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        //ERC1155 0xd9b67a26 interface id
        val data = Numeric.hexStringToByteArray("0x5b5e139f") // ERC721 interface id
        val interfaceId = Bytes4(data)
        return erc721URIStorageWrapper.supportsInterface(interfaceId).send().value
    }

    private fun loadContract(chain: Chain, address: String) : ERC721URIStorage{
        val basicWeb3Instance  = RopstenWeb3Instance()
        val web3j = basicWeb3Instance.getWeb3j()

        val credentials: Credentials = Credentials.create("d720ef2cb49c6cbe94175ed413d27e635c5acaa1b7cf03d1faad3a0abc2f53f3")

        val gasProvider: ContractGasProvider = WaltIdGasProvider

        return ERC721URIStorage.load(address, web3j, credentials, gasProvider)

    }


}