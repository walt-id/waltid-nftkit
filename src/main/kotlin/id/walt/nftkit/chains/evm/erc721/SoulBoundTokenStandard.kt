package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.Values
import id.walt.nftkit.services.DeploymentResponse
import id.walt.nftkit.services.EVMChain
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.services.WaltIdServices
import id.walt.nftkit.utilis.WaltIdGasProvider
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Bytes4
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import smart_contract_wrapper.SoulBoundTest
import java.math.BigInteger

object SoulBoundTokenStandard : ISoulBoundTokenStandard {


    private fun loadContract(chain: EVMChain, address: String, signedAccount: String? = ""): SoulBoundTest {
        val web3j = ProviderFactory.getProvider(chain).getWeb3j()

        val privateKey: String = if (signedAccount == null || "" == (signedAccount)) {
            WaltIdServices.loadChainConfig().privateKey
        } else {
            val lowercaseAddress = WaltIdServices.loadAccountKeysConfig().keys.mapKeys { it.key.lowercase() }
            lowercaseAddress[signedAccount.lowercase()]!!
        }

        val credentials: Credentials = Credentials.create(privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val chainId = when (chain) {
            EVMChain.ETHEREUM -> Values.ETHEREUM_MAINNET_CHAIN_ID
            EVMChain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_CHAIN_ID
            EVMChain.SEPOLIA -> Values.ETHEREUM_TESTNET_SEPOLIA_CHAIN_ID
            EVMChain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
            EVMChain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            EVMChain.ASTAR -> Values.ASTAR_MAINNET_CHAIN_ID
            EVMChain.MOONBEAM -> Values.MOONBEAM_MAINNET_CHAIN_ID
            EVMChain.SHIMMEREVM -> Values.SHIMMEREVM_TESTNET_CHAIN_ID
        }
        val transactionManager: TransactionManager = RawTransactionManager(
            web3j, credentials, chainId
        )
        return SoulBoundTest.load(address, web3j, transactionManager, gasProvider)

    }


    override fun ownerOf(chain: EVMChain, contractAddress: String, tokenId: Uint256): String? {
        return loadContract(chain, contractAddress).ownerOf(tokenId).send().value
    }

    override fun name(chain: EVMChain, contractAddress: String): String? {
        val contract = loadContract(chain, contractAddress)
        return contract.name().send().value
    }

    override fun symbol(chain: EVMChain, contractAddress: String): String? {
        return loadContract(chain, contractAddress).symbol().send().value
    }

    override fun tokenURI(chain: EVMChain, contractAddress: String, tokenId: Uint256): String? {
        return loadContract(chain, contractAddress).tokenURI(tokenId).send().value
    }

    override fun balanceOf(chain: EVMChain, contractAddress: String, owner: Address): BigInteger? {
        return loadContract(chain, contractAddress).balanceOf(owner).send().value
    }

    override fun safeTransferFrom(
        chain: EVMChain,
        contractAddress: String,
        from: Address,
        to: Address,
        tokenId: Uint256,
        signedAccount: String?
    ): TransactionReceipt {
        TODO("Not yet implemented")
    }

    override fun safeMint(chain: EVMChain, contractAddress: String, to: String, uri: String): TransactionReceipt? {
        return loadContract(chain, contractAddress).safeMint(to, uri).send()
    }


    fun deployContract(chain: EVMChain): DeploymentResponse {
        val chainId = when (chain) {
            EVMChain.ETHEREUM -> Values.ETHEREUM_MAINNET_CHAIN_ID
            EVMChain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_CHAIN_ID
            EVMChain.SEPOLIA -> Values.ETHEREUM_TESTNET_SEPOLIA_CHAIN_ID
            EVMChain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
            EVMChain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            EVMChain.ASTAR -> Values.ASTAR_MAINNET_CHAIN_ID
            EVMChain.MOONBEAM -> Values.MOONBEAM_MAINNET_CHAIN_ID
            EVMChain.SHIMMEREVM -> Values.SHIMMEREVM_TESTNET_CHAIN_ID
        }

        val web3j = ProviderFactory.getProvider(chain).getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<SoulBoundTest>
        val transactionManager: TransactionManager = RawTransactionManager(
            web3j, credentials, chainId
        )
        remotCall = SoulBoundTest.deploy(web3j, transactionManager, gasProvider)

        val contract = remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(
            contract.transactionReceipt.get().transactionHash,
            "$url/tx/${contract.transactionReceipt.get().transactionHash}"
        )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}")
    }


    override fun supportsInterface(chain: EVMChain, contractAddress: String): Boolean {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        val data = Numeric.hexStringToByteArray("0x5b5e139f") // ERC721 interface id
        val interfaceId = Bytes4(data)
        return erc721URIStorageWrapper.supportsInterface(interfaceId).send().value
    }
}
