package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes4
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import smart_contract_wrapper.CustomAccessControlERC721
import smart_contract_wrapper.CustomOwnableERC721
import java.math.BigInteger


object Erc721TokenStandard : IErc721TokenStandard {


    fun deployContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        /*val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall : RemoteCall<ERC721URIStorage>
        if(chain == Chain.POLYGON || chain == Chain.MUMBAI){
            val chainId : Long
            if(chain == Chain.POLYGON){
                chainId = Values.POLYGON_MAINNET_CHAIN_ID
            }else{
                chainId = Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
            }
            val transactionManager: TransactionManager = RawTransactionManager(
                web3j, credentials, chainId
            )
            remotCall = ERC721URIStorage.deploy(web3j,transactionManager,gasProvider,Utf8String(parameter.name),Utf8String(parameter.symbol))
        }else{
            remotCall = ERC721URIStorage.deploy(web3j,credentials,gasProvider,Utf8String(parameter.name),Utf8String(parameter.symbol))
        }
        val contract= remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(contract.transactionReceipt.get().transactionHash,  "$url/tx/${contract.transactionReceipt.get().transactionHash}" )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}" )*/
        /*if(options.accessControl == AccessControl.OWNABLE){
            return deployOwnableContract(chain, parameter, options)
        }else{
            return deployRBACContract(chain, parameter, options)
        }*/
        return when (options.accessControl) {
            AccessControl.OWNABLE -> deployOwnableContract(chain, parameter, options)
            AccessControl.ROLE_BASED_ACCESS_CONTROL -> deployRBACContract(chain, parameter, options)
        }

    }

    override fun mintToken(
        chain: Chain,
        contractAddress: String,
        recipient: Address,
        tokenURI: Utf8String
    ): TransactionReceipt? {
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

    override fun tokenURI(chain: Chain, contractAddress: String, tokenId: Uint256): String {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.tokenURI(tokenId).send().value
    }

    override fun balanceOf(chain: Chain, contractAddress: String, owner: Address): BigInteger? {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        return erc721URIStorageWrapper.balanceOf(owner).send().value
    }

    override fun updateTokenUri(
        chain: Chain,
        contractAddress: String,
        token: BigInteger,
        tokenURI: Utf8String,
        signedAccount: String?
    ): TransactionReceipt {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc721URIStorageWrapper.setTokenURI(Uint256(token), tokenURI).send()
    }

    override fun supportsInterface(chain: Chain, contractAddress: String) : Boolean {
        val erc721URIStorageWrapper = loadContract(chain, contractAddress)
        val data = Numeric.hexStringToByteArray("0x5b5e139f") // ERC721 interface id
        val interfaceId = Bytes4(data)
        return erc721URIStorageWrapper.supportsInterface(interfaceId).send().value
    }

    fun deployOwnableContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<CustomOwnableERC721>
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

    fun deployRBACContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<CustomAccessControlERC721>
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

    private fun loadContract(chain: Chain, address: String, signedAccount: String? ="") : CustomOwnableERC721 {
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

             return  CustomOwnableERC721.load(address, web3j,transactionManager,gasProvider)
        }else{
            return CustomOwnableERC721.load(address, web3j,credentials,gasProvider)

        }
    }


}
