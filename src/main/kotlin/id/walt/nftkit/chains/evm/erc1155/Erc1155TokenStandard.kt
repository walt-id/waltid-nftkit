package id.walt.nftkit.chains.evm.erc1155

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.generated.Bytes4
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import smart_contract_wrapper.CustomAccessControlERC1155
import smart_contract_wrapper.CustomOwnableERC1155
import java.math.BigInteger

object Erc1155TokenStandard {

    fun deployContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        return when (options.accessControl) {
            AccessControl.OWNABLE -> deployOwnableContract(chain, parameter, options)
            AccessControl.ROLE_BASED_ACCESS_CONTROL -> deployRBACContract(chain, parameter, options)
        }
    }

    fun mint (
        chain: Chain,
        contractAddress: String,
        account : Address,
        amount : Uint256,
        tokenURI : Utf8String,
        data : DynamicBytes
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.mint(account, amount,tokenURI,data).send()
    }

    fun mintBatch (
        chain: Chain,
        contractAddress: String,
        to : Address,
        ids : DynamicArray<Uint256>,
        amounts : DynamicArray<Uint256>,
        data : DynamicBytes
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.mintBatch(to,ids,amounts,data).send()
    }


    fun balanceOf(chain: Chain, contractAddress: String, account: Address, id: Uint256): BigInteger? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOf(account,id).send().value
    }


    fun balanceOfBatch(chain: Chain, contractAddress: String, accounts: DynamicArray<Address>, ids: DynamicArray<Uint256>): MutableList<Uint256>? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOfBatch(accounts,ids).send().value
    }



    fun setApprovalForAll(chain: Chain, contractAddress: String, operator : Address, approved :Bool): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.setApprovalForAll(operator,approved).send()

    }

    fun isApprovalForAll(chain: Chain, contractAddress: String, account: Address, operator : Address) : Boolean {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.isApprovedForAll(account,operator).send().value

    }

    fun safeTransferFrom (
        chain: Chain,
        contractAddress: String,
        from : Address,
        to : Address,
        id : Uint256,
        amount : Uint256,
        data : DynamicBytes
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.safeTransferFrom(from, to, id, amount, data).send()

    }



    fun safeBatchTransferFrom (
        chain: Chain,
        contractAddress: String,
        from : Address,
        to : Address,
        ids : DynamicArray<Uint256>,
        amounts : DynamicArray<Uint256>,
        data : DynamicBytes
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.safeBatchTransferFrom (from, to, ids, amounts, data ).send()

    }


    fun uri(chain: Chain, contractAddress: String, tokenId: Uint256): String {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.uri(tokenId).send().value
    }

     fun updateUri(
        chain: Chain,
        contractAddress: String,
        tokenId: Uint256,
        tokenURI: Utf8String,
        signedAccount: String?
    ): TransactionReceipt {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc1155URIStorageWrapper.setURI(tokenId, tokenURI).send()
    }
     fun supportsInterface(chain: Chain, contractAddress: String) : Boolean {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        val data = Numeric.hexStringToByteArray("0xd9b67a26") // ERC1155 interface id
        val interfaceId = Bytes4(data)
        return erc1155URIStorageWrapper.supportsInterface(interfaceId).send().value
    }


    fun deployOwnableContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)
        val gasProvider: ContractGasProvider = WaltIdGasProvider
        val remotCall: RemoteCall<CustomOwnableERC1155>
        val chainId: Long = when (chain) {

            Chain.ETHEREUM-> Values.ETHEREUM_MAINNET_CHAIN_ID
            Chain.ROPSTEN-> Values.ETHEREUM_TESTNET_ROPSTEN_CHAIN_ID
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_CHAIN_ID
            Chain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
        }

        val transactionManager: TransactionManager = RawTransactionManager(web3j, credentials, chainId )

        remotCall = CustomOwnableERC1155.deploy(
            web3j,
            transactionManager,
            gasProvider,
            Bool(parameter.options.burnable),
            Bool(parameter.options.transferable)
        )

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
        val remotCall: RemoteCall<CustomAccessControlERC1155>

        val chainId: Long = when (chain) {

            Chain.ETHEREUM-> Values.ETHEREUM_MAINNET_CHAIN_ID
            Chain.ROPSTEN-> Values.ETHEREUM_TESTNET_ROPSTEN_CHAIN_ID
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_CHAIN_ID
            Chain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
        }

        val transactionManager: TransactionManager = RawTransactionManager(web3j, credentials, chainId)
        remotCall = CustomAccessControlERC1155.deploy(
            web3j,
            transactionManager,
            gasProvider,
            Bool(parameter.options.burnable),
            Bool(parameter.options.transferable)
        )

        val contract = remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(
            contract.transactionReceipt.get().transactionHash,
            "$url/tx/${contract.transactionReceipt.get().transactionHash}"
        )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}")
    }



    private fun loadContract(chain: Chain, address: String, signedAccount: String? ="") : CustomOwnableERC1155 {
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

        val chainId: Long = when (chain) {

            Chain.ETHEREUM-> Values.ETHEREUM_MAINNET_CHAIN_ID
            Chain.ROPSTEN-> Values.ETHEREUM_TESTNET_ROPSTEN_CHAIN_ID
            Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_CHAIN_ID
            Chain.POLYGON -> Values.POLYGON_MAINNET_CHAIN_ID
            Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_CHAIN_ID
        }

        val transactionManager: TransactionManager = RawTransactionManager (web3j, credentials, chainId)

        return  CustomOwnableERC1155.load(address, web3j,transactionManager,gasProvider)


    }
}