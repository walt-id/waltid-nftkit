package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import smart_contract_wrapper.CustomOwnableERC721
import smart_contract_wrapper.SoulBoundTest
import java.math.BigInteger

object SoulBoundTokenStandard :  ISoulBoundTokenStandard  {






    private fun loadContract(chain: EVMChain, address: String, signedAccount: String? ="") : SoulBoundTest {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        val privateKey: String = if(signedAccount == null || "" == (signedAccount)){
            WaltIdServices.loadChainConfig().privateKey
        }else{
            val lowercaseAddress= WaltIdServices.loadAccountKeysConfig().keys.mapKeys { it.key.lowercase() }
            lowercaseAddress[signedAccount.lowercase()]!!
        }

        val credentials: Credentials = Credentials.create(privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val chainId= when(chain){
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
        return  SoulBoundTest.load(address, web3j,transactionManager,gasProvider)

    }

    }
    override fun mintToken(
        chain: EVMChain,
        contractAddress: String,
        recipient: Address,
        tokenURI: Utf8String
    ): TransactionReceipt? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.mintTo(recipient, tokenURI).send()
    }

    override fun ownerOf(chain: EVMChain, contractAddress: String, tokenId: Uint256): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.ownerOf(tokenId).send().value
    }

    override fun name(chain: EVMChain, contractAddress: String): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.name().send().value
    }

    override fun symbol(chain: EVMChain, contractAddress: String): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.symbol().send().value
    }

    override fun tokenURI(chain: EVMChain, contractAddress: String, tokenId: Uint256): String? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.tokenURI(tokenId).send().value
    }

    override fun balanceOf(chain: EVMChain, contractAddress: String, owner: Address): BigInteger? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.balanceOf(owner).send().value
    }

    override fun supportsInterface(chain: EVMChain, contractAddress: String): Boolean {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        val data = Numeric.hexStringToByteArray("0x5b5e139f") // ERC721 interface id
        val interfaceId = Bytes4(data)
        return erc721URIStorageWrapper.supportsInterface(interfaceId).send().value
    }

    override fun updateTokenUri(
        chain: EVMChain,
        contractAddress: String,
        token: BigInteger,
        tokenURI: Utf8String,
        signedAccount: String?
    ): TransactionReceipt? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.setTokenURI(Uint256(token), tokenURI).send()
    }

    override fun transferFrom(
        chain: EVMChain,
        contractAddress: String,
        from: Address,
        to: Address,
        tokenId: Uint256,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.transferFrom(from, to, tokenId).send()
    }

    override fun safeTransferFrom(
        chain: EVMChain,
        contractAddress: String,
        from: Address,
        to: Address,
        tokenId: Uint256,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return  erc721URIStorageWrapper.safeTransferFrom(from, to, tokenId).send()
    }

    override fun safeTransferFrom(
        chain: EVMChain,
        contractAddress: String,
        from: Address,
        to: Address,
        tokenId: Uint256,
        data: DynamicBytes,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.safeTransferFrom(from, to, tokenId, data).send()
    }

    override fun setApprovalForAll(
        chain: EVMChain,
        contractAddress: String,
        operator: Address,
        approved: Bool,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.setApprovalForAll(operator, approved).send()
    }

    override fun isApprovedForAll(chain: EVMChain, contractAddress: String, owner: Address, operator: Address): Bool {
        TODO("Not yet implemented")
    }

    override fun approve(
        chain: EVMChain,
        contractAddress: String,
        to: Address,
        tokenId: Uint256,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.approve(to, tokenId).send()
    }

    override fun getApproved(chain: EVMChain, contractAddress: String, tokenId: Uint256): Address {
        TODO("Not yet implemented")
    }

    private fun loadContract(chain: EVMChain, address: String, signedAccount: String? ="") : CustomOwnableERC721 {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        val privateKey: String
        if(signedAccount == null || "".equals(signedAccount)){
            privateKey= WaltIdServices.loadChainConfig().privateKey
        }else{
            val lowercaseAddress= WaltIdServices.loadAccountKeysConfig().keys.mapKeys { it.key.lowercase() }
            privateKey= lowercaseAddress.get(signedAccount.lowercase())!!
            if(privateKey == null){
                throw Exception("Account not found")
            }
        }

        val credentials: Credentials = Credentials.create(privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val chainId= when(chain){
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
        return  CustomOwnableERC721.load(address, web3j,transactionManager,gasProvider)
        /*if (chain == EVMChain.POLYGON || chain == EVMChain.MUMBAI) {
            val chainId: Long
            if (chain == EVMChain.POLYGON) {
                chainId = Values.POLYGON_MAINNET_CHAIN_ID
            } else {
                chainId = Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            }
            val transactionManager: TransactionManager = RawTransactionManager(
                web3j, credentials, chainId
            )

             return  CustomOwnableERC721.load(address, web3j,transactionManager,gasProvider)
        }else{
            return CustomOwnableERC721.load(address, web3j,credentials,gasProvider)

        }*/
    }

    fun deployOwnableContract(chain: EVMChain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<CustomOwnableERC721>
        if (chain == EVMChain.SHIMMEREVM) {
            val chainId: Long
            chainId = Values.SHIMMEREVM_TESTNET_CHAIN_ID
            val transactionManager: TransactionManager = RawTransactionManager(
                web3j, credentials, chainId
            )
            remotCall = CustomOwnableERC721.deploy(
                web3j,
                transactionManager,
                gasProvider,
                Utf8String(parameter.name),
                Utf8String(parameter.symbol),
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        } else {
            remotCall = CustomOwnableERC721.deploy(
                web3j,
                credentials,
                gasProvider,
                Utf8String(parameter.name),
                Utf8String(parameter.symbol),
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        }
        val contract = remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(
            contract.transactionReceipt.get().transactionHash,
            "$url/tx/${contract.transactionReceipt.get().transactionHash}"
        )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}")

    }

    fun deployRBACContract(chain: EVMChain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<CustomAccessControlERC721>
        if (chain == EVMChain.SHIMMEREVM ) {
            val chainId: Long
            chainId = Values.SHIMMEREVM_TESTNET_CHAIN_ID
            val transactionManager: TransactionManager = RawTransactionManager(
                web3j, credentials, chainId
            )
            remotCall = CustomAccessControlERC721.deploy(
                web3j,
                transactionManager,
                gasProvider,
                Utf8String(parameter.name),
                Utf8String(parameter.symbol),
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        } else {
            remotCall = CustomAccessControlERC721.deploy(
                web3j,
                credentials,
                gasProvider,
                Utf8String(parameter.name),
                Utf8String(parameter.symbol),
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        }
        val contract = remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(
            contract.transactionReceipt.get().transactionHash,
            "$url/tx/${contract.transactionReceipt.get().transactionHash}"
        )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}")
    }

}