package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.utilis.Common.getTransactionResponse
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.bouncycastle.util.encoders.Hex
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import smart_contract_wrapper.AccessControl
import smart_contract_wrapper.Ownable

object AccessControlService {

    fun transferOwnership(chain: Chain, contractAddress: String, account: String): TransactionResponse {
        val ownableWrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = ownableWrapper.transferOwnership(Address(account)).send()
        return getTransactionResponse(chain, transactionReceipt)
    }

    fun renounceOwnership(chain: Chain, contractAddress: String): TransactionResponse {
        val ownableWrapper = loadOwnableContract(chain, contractAddress)
        val transactionReceipt = ownableWrapper.renounceOwnership().send()
        /*val url = WaltIdServices.getBlockExplorerUrl(chain)
        return TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt!!.transactionHash}" )*/
        return getTransactionResponse(chain, transactionReceipt)
    }

    fun owner(chain: Chain, contractAddress: String): String {
        val ownableWrapper = loadOwnableContract(chain, contractAddress)
        val owner= ownableWrapper.owner().send().value
        return owner
    }

    fun grantRole(chain: Chain, contractAddress: String, role: String, account: String): TransactionResponse {
        val rabcWrapper = loadRbacContract(chain, contractAddress)
        val hash = getHashedRole(role)
        val transactionReceipt= rabcWrapper.grantRole(Bytes32(hash), Address(account)).send()
        /*val url = WaltIdServices.getBlockExplorerUrl(chain)
        return TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt!!.transactionHash}" )*/
        return getTransactionResponse(chain, transactionReceipt)
    }

    fun renounceRole(chain: Chain, contractAddress: String, role: String, account: String): TransactionResponse {
        val rabcWrapper = loadRbacContract(chain, contractAddress)
        val hash = getHashedRole(role)
        val transactionReceipt= rabcWrapper.renounceRole(Bytes32(hash), Address(account)).send()
        return getTransactionResponse(chain, transactionReceipt)
    }

    fun revokeRole(chain: Chain, contractAddress: String, role: String, account: String): TransactionResponse {
        val rabcWrapper = loadRbacContract(chain, contractAddress)
        val hash = getHashedRole(role)
        val transactionReceipt= rabcWrapper.revokeRole(Bytes32(hash), Address(account)).send()
        return getTransactionResponse(chain, transactionReceipt)
    }

    fun getRoleAdmin(chain: Chain, contractAddress: String, role:String): String {
        val rabcWrapper = loadRbacContract(chain, contractAddress)
        val hash = getHashedRole(role)
        return rabcWrapper.getRoleAdmin(Bytes32(hash)).send().value.joinToString(separator = "")
    }

    fun hasRole(chain: Chain, contractAddress: String, role:String, account: String): Boolean {
        val rabcWrapper = loadRbacContract(chain, contractAddress)
        val hash = getHashedRole(role)
        return rabcWrapper.hasRole(Bytes32(hash), Address(account)).send().value
    }

    private fun getHashedRole(role: String): ByteArray {
        return when(role){
            "DEFAULT_ADMIN_ROLE" -> ByteArray(32)
            else -> Numeric.hexStringToByteArray(Hash.sha3(Hex.toHexString(role.toByteArray())))
        }
    }


    private fun loadOwnableContract(chain: Chain, address: String) : Ownable {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider

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
            return  Ownable.load(address, web3j,transactionManager,gasProvider)
        }else{
            return Ownable.load(address, web3j,credentials,gasProvider)
        }
    }

    private fun loadRbacContract(chain: Chain, address: String) : AccessControl {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        val credentials: Credentials = Credentials.create(WaltIdServices.loadChainConfig().privateKey)

        val gasProvider: ContractGasProvider = WaltIdGasProvider

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
            return  smart_contract_wrapper.AccessControl.load(address, web3j,transactionManager,gasProvider)
        }else{
            return smart_contract_wrapper.AccessControl.load(address, web3j,credentials,gasProvider)
        }
    }



}