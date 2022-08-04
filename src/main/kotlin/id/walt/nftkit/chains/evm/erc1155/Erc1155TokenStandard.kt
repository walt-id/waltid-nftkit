package id.walt.nftkit.chains.evm.erc1155

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Bytes
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
import smart_contract_wrapper.CustomAccessControlERC1155
//import smart_contract_wrapper.CustomOwnableERC1155
import java.math.BigInteger

object Erc1155TokenStandard {

    fun deployContract(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        return when (options.accessControl) {
            AccessControl.OWNABLE -> deployOwnableContract(chain, parameter, options)
            AccessControl.ROLE_BASED_ACCESS_CONTROL -> deployRBACContract(chain, parameter, options)
        }
    }

    override fun mint (
        chain: Chain,
        contractAddress: String,
        account : Address,
        anmount : Uint256,
        tokenURI : Utf8String,
        data : Byte
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.mint(account, amount,tokenURI,data).send()
    }

    override fun mintBatch (
        chain: Chain,
        contractAddress: String,
        to : Address,
        ids : Uint256,
        amounts : Utf8String,
        data : Bytes
    ): TransactionReceipt? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.mintBtch(to,ids,amounts,data).send()
    }


    override fun balanceOf(chain: Chain, contractAddress: String, account: Address, id: Uint256): BigInteger? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOf(account,id).send().value
    }

    override fun balanceOfBtach(chain: Chain, contractAddress: String, accounts: Address, ids: Uint256 ): BigInteger? {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOfBtach(accounts,ids).send().value
    }


    override fun setApprovalForAll(chain: Chain, contractAddress: String, operator : Address, approved :Bool )  {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOfBtach(operator,approved).send().value

    }

    override fun isApprovalForAll(chain: Chain, contractAddress: String, account: Address, operator : Address) : Boolean {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.balanceOfBtach(account,operator).send().value

    }

    override fun safeTransferFrom (
        chain: Chain,
        contractAddress: String,
        from : Address,
        to : Address,
        id : Uint256,
        amount : Uint256,
        data : Bytes
    ) {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.tokenURI(from, to, id, amount, data ).send().value

    }



    override fun safeBatchTransferFrom (
        chain: Chain,
        contractAddress: String,
        from : Address,
        to : Address,
        ids : Uint256,
        amounts : Uint256,
        data : Bytes
    ) {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.tokenURI(from, to, ids, amounts, data ).send().value

    }


    override fun uri(chain: Chain, contractAddress: String, tokenId: Uint256): String {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress)
        return erc1155URIStorageWrapper.tokenURI(tokenId).send().value
    }

    override fun updateUri(
        chain: Chain,
        contractAddress: String,
        token: BigInteger,
        tokenURI: Utf8String,
        signedAccount: String?
    ): TransactionReceipt {
        val erc1155URIStorageWrapper = loadContract(chain, contractAddress, signedAccount)
        return erc1155URIStorageWrapper.setTokenURI(Uint256(token), tokenURI).send()
    }
    override fun supportsInterface(chain: Chain, contractAddress: String) : Boolean {
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
            remotCall = CustomOwnableERC1155.deploy(
                web3j,
                transactionManager,
                gasProvider,
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        } else {
            remotCall = CustomOwnableERC1155.deploy(
                web3j,
                credentials,
                gasProvider,
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
        val remotCall: RemoteCall<CustomAccessControlERC1155>
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
            remotCall = CustomAccessControlERC1155.deploy(
                web3j,
                transactionManager,
                gasProvider,
                Bool(parameter.options.burnable),
                Bool(parameter.options.transferable)
            )
        } else {
            remotCall = CustomAccessControlERC1155.deploy(
                web3j,
                credentials,
                gasProvider,
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

            return  CustomOwnableERC1155.load(address, web3j,transactionManager,gasProvider)
        }else{
            return CustomOwnableERC1155.load(address, web3j,credentials,gasProvider)

        }
    }
}