package id.walt.nftkit.smart_contract_wrapper

import io.reactivex.Flowable
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Bytes4
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.BaseEventResponse
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.util.*

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 4.9.0.
 */
class Erc721OnchainCredentialWrapper : Contract {
    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        credentials: Credentials?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?
    ) : super(
        BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit
    )

    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        credentials: Credentials?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, credentials, contractGasProvider
    )

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        transactionManager: TransactionManager?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?
    ) : super(
        BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit
    )

    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        transactionManager: TransactionManager?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, transactionManager, contractGasProvider
    )

    fun getApprovalEvents(transactionReceipt: TransactionReceipt?): List<ApprovalEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.owner = eventValues.indexedValues[0] as Address
            typedResponse.approved = eventValues.indexedValues[1] as Address
            typedResponse.tokenId = eventValues.indexedValues[2] as Uint256
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalEventFlowable(filter: EthFilter?): Flowable<ApprovalEventResponse> {
        return web3j.ethLogFlowable(filter).map {
            val eventValues = extractEventParametersWithLog(APPROVAL_EVENT, it)
            ApprovalEventResponse().apply {
                log = it
                owner = eventValues.indexedValues[0] as Address
                approved = eventValues.indexedValues[1] as Address
                tokenId = eventValues.indexedValues[2] as Uint256
            }
        }
    }

    fun approvalEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<ApprovalEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT))
        return approvalEventFlowable(filter)
    }

    fun getApprovalForAllEvents(transactionReceipt: TransactionReceipt?): List<ApprovalForAllEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalForAllEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalForAllEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.owner = eventValues.indexedValues[0] as Address
            typedResponse.operator = eventValues.indexedValues[1] as Address
            typedResponse.approved = eventValues.nonIndexedValues[0] as Bool
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalForAllEventFlowable(filter: EthFilter?): Flowable<ApprovalForAllEventResponse> =
        web3j.ethLogFlowable(filter).map {
            val eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, it)
            ApprovalForAllEventResponse().apply {
                log = it
                owner = eventValues.indexedValues[0] as Address
                operator = eventValues.indexedValues[1] as Address
                approved = eventValues.nonIndexedValues[0] as Bool
            }
        }

    fun approvalForAllEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<ApprovalForAllEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT))
        return approvalForAllEventFlowable(filter)
    }

    fun getOwnershipTransferredEvents(transactionReceipt: TransactionReceipt?): List<OwnershipTransferredEventResponse> {
        val valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt)
        val responses = ArrayList<OwnershipTransferredEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = OwnershipTransferredEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.previousOwner = eventValues.indexedValues[0] as Address
            typedResponse.newOwner = eventValues.indexedValues[1] as Address
            responses.add(typedResponse)
        }
        return responses
    }

    fun ownershipTransferredEventFlowable(filter: EthFilter?): Flowable<OwnershipTransferredEventResponse> {
        return web3j.ethLogFlowable(filter).map {
            val eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, it)
            OwnershipTransferredEventResponse().apply {
                log = it
                previousOwner = eventValues.indexedValues[0] as Address
                newOwner = eventValues.indexedValues[1] as Address
            }
        }
    }

    fun ownershipTransferredEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<OwnershipTransferredEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT))
        return ownershipTransferredEventFlowable(filter)
    }

    fun getTransferEvents(transactionReceipt: TransactionReceipt?): List<TransferEventResponse> {
        val valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt)
        val responses = ArrayList<TransferEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = TransferEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.from = eventValues.indexedValues[0] as Address
            typedResponse.to = eventValues.indexedValues[1] as Address
            typedResponse.tokenId = eventValues.indexedValues[2] as Uint256
            responses.add(typedResponse)
        }
        return responses
    }

    fun transferEventFlowable(filter: EthFilter?): Flowable<TransferEventResponse> =
        web3j.ethLogFlowable(filter).map {
            val eventValues = extractEventParametersWithLog(TRANSFER_EVENT, it)
            TransferEventResponse().apply {
                log = it
                from = eventValues.indexedValues[0] as Address
                to = eventValues.indexedValues[1] as Address
                tokenId = eventValues.indexedValues[2] as Uint256
            }
        }

    fun transferEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<TransferEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT))
        return transferEventFlowable(filter)
    }

    fun approve(to: Address, tokenId: Uint256): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_APPROVE,
            Arrays.asList<Type<*>>(to, tokenId), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun balanceOf(owner: Address): RemoteFunctionCall<Uint256> {
        val function = Function(
            FUNC_BALANCEOF,
            Arrays.asList<Type<*>>(owner),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun getApproved(tokenId: Uint256): RemoteFunctionCall<Address> {
        val function = Function(
            FUNC_GETAPPROVED,
            Arrays.asList<Type<*>>(tokenId),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun isApprovedForAll(owner: Address, operator: Address): RemoteFunctionCall<Bool> {
        val function = Function(
            FUNC_ISAPPROVEDFORALL,
            Arrays.asList<Type<*>>(owner, operator),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Bool?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun mintTo(_recipient: Address, tokenURI: Utf8String): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_MINTTO,
            Arrays.asList<Type<*>>(_recipient, tokenURI), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun name(): RemoteFunctionCall<Utf8String> {
        val function = Function(
            FUNC_NAME,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun owner(): RemoteFunctionCall<Address> {
        val function = Function(
            FUNC_OWNER,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun ownerOf(tokenId: Uint256): RemoteFunctionCall<Address> {
        val function = Function(
            FUNC_OWNEROF,
            Arrays.asList<Type<*>>(tokenId),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun renounceOwnership(): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_RENOUNCEOWNERSHIP,
            Arrays.asList(), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun safeTransferFrom(from: Address, to: Address, tokenId: Uint256): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_safeTransferFrom,
            Arrays.asList<Type<*>>(from, to, tokenId), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun safeTransferFrom(
        from: Address,
        to: Address,
        tokenId: Uint256,
        _data: DynamicBytes
    ): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_safeTransferFrom,
            Arrays.asList<Type<*>>(from, to, tokenId, _data), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun setApprovalForAll(operator: Address, approved: Bool): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_SETAPPROVALFORALL,
            Arrays.asList<Type<*>>(operator, approved), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun supportsInterface(interfaceId: Bytes4): RemoteFunctionCall<Bool> {
        val function = Function(
            FUNC_SUPPORTSINTERFACE,
            Arrays.asList<Type<*>>(interfaceId),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Bool?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun symbol(): RemoteFunctionCall<Utf8String> {
        val function = Function(
            FUNC_SYMBOL,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun tokenURI(tokenId: Uint256): RemoteFunctionCall<Utf8String> {
        val function = Function(
            FUNC_TOKENURI,
            Arrays.asList<Type<*>>(tokenId),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {})
        )
        return executeRemoteCallSingleValueReturn(function)
    }

    fun transferFrom(from: Address, to: Address, tokenId: Uint256): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFERFROM,
            Arrays.asList<Type<*>>(from, to, tokenId), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun transferOwnership(newOwner: Address): RemoteFunctionCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFEROWNERSHIP,
            Arrays.asList<Type<*>>(newOwner), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    class ApprovalEventResponse : BaseEventResponse() {
        var owner: Address? = null
        var approved: Address? = null
        var tokenId: Uint256? = null
    }

    class ApprovalForAllEventResponse : BaseEventResponse() {
        var owner: Address? = null
        var operator: Address? = null
        var approved: Bool? = null
    }

    class OwnershipTransferredEventResponse : BaseEventResponse() {
        var previousOwner: Address? = null
        var newOwner: Address? = null
    }

    class TransferEventResponse : BaseEventResponse() {
        var from: Address? = null
        var to: Address? = null
        var tokenId: Uint256? = null
    }

    companion object {
        const val BINARY =
            "60806040523480156200001157600080fd5b50604080518082018252601481527f56657269666961626c6543726564656e7469616c00000000000000000000000060208083019182528351808501909452600284527f56430000000000000000000000000000000000000000000000000000000000009084015281519192916200008c916000916200012d565b508051620000a29060019060208401906200012d565b505050620000d1620000c2620000d7640100000000026401000000009004565b640100000000620000db810204565b62000229565b3390565b60078054600160a060020a03838116600160a060020a0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b8280546200013b90620001d3565b90600052602060002090601f0160209004810192826200015f5760008555620001aa565b82601f106200017a57805160ff1916838001178555620001aa565b82800160010185558215620001aa579182015b82811115620001aa5782518255916020019190600101906200018d565b50620001b8929150620001bc565b5090565b5b80821115620001b85760008155600101620001bd565b600281046001821680620001e857607f821691505b6020821081141562000223577f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b50919050565b61150f80620002396000396000f3fe608060405234801561001057600080fd5b5060043610610127576000357c01000000000000000000000000000000000000000000000000000000009004806370a08231116100bf578063a22cb4651161008e578063a22cb4651461021f578063b88d4fde14610232578063c87b56dd14610240578063e985e9c514610253578063f2fde38b1461028f57600080fd5b806370a08231146101eb578063715018a6146101fe5780638da5cb5b1461020657806395d89b411461021757600080fd5b8063095ea7b3116100fb578063095ea7b3146101b557806323b872dd146101ca57806342842e0e146101ca5780636352211e146101d857600080fd5b806275a3171461012c57806301ffc9a71461015257806306fdde0314610175578063081812fc1461018a575b600080fd5b61013f61013a366004611069565b6102a2565b6040519081526020015b60405180910390f35b6101656101603660046110cb565b6102da565b6040519015158152602001610149565b61017d6103b6565b604051610149919061113a565b61019d61019836600461116d565b610448565b604051600160a060020a039091168152602001610149565b6101c86101c3366004611186565b6104fb565b005b6101c86101c33660046111b0565b61019d6101e636600461116d565b61054b565b61013f6101f93660046111ec565b6105de565b6101c8610680565b600754600160a060020a031661019d565b61017d6106ee565b6101c861022d366004611207565b6106fd565b6101c86101c3366004611243565b61017d61024e36600461116d565b61070c565b6101656102613660046112bf565b600160a060020a03918216600090815260056020908152604080832093909416825291909152205460ff1690565b6101c861029d3660046111ec565b6107d6565b60006102b2600880546001019055565b60006102bd60085490565b90506102c984826108c8565b6102d38184610a27565b9392505050565b60007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1982167f80ac58cd00000000000000000000000000000000000000000000000000000000148061036757507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1982167f5b5e139f00000000000000000000000000000000000000000000000000000000145b806103b057507f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff198316145b92915050565b6060600080546103c5906112f2565b80601f01602080910402602001604051908101604052809291908181526020018280546103f1906112f2565b801561043e5780601f106104135761010080835404028352916020019161043e565b820191906000526020600020905b81548152906001019060200180831161042157829003601f168201915b5050505050905090565b600081815260026020526040812054600160a060020a03166104df576040516000805160206114ba833981519152815260206004820152602c60248201527f4552433732313a20617070726f76656420717565727920666f72206e6f6e657860448201527f697374656e7420746f6b656e000000000000000000000000000000000000000060648201526084015b60405180910390fd5b50600090815260046020526040902054600160a060020a031690565b6040516000805160206114ba833981519152815260206004820152601560248201527f4e6f6e7472616e7366657261626c6520746f6b656e000000000000000000000060448201526064016104d6565b600081815260026020526040812054600160a060020a0316806103b0576040516000805160206114ba833981519152815260206004820152602960248201527f4552433732313a206f776e657220717565727920666f72206e6f6e657869737460448201527f656e7420746f6b656e000000000000000000000000000000000000000000000060648201526084016104d6565b6000600160a060020a038216610664576040516000805160206114ba833981519152815260206004820152602a60248201527f4552433732313a2062616c616e636520717565727920666f7220746865207a6560448201527f726f20616464726573730000000000000000000000000000000000000000000060648201526084016104d6565b50600160a060020a031660009081526003602052604090205490565b600754600160a060020a031633146106e2576040516000805160206114ba833981519152815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657260448201526064016104d6565b6106ec6000610add565b565b6060600180546103c5906112f2565b6107083383836104fb565b5050565b600081815260026020526040902054606090600160a060020a03166107a1576040516000805160206114ba833981519152815260206004820152603160248201527f45524337323155524953746f726167653a2055524920717565727920666f722060448201527f6e6f6e6578697374656e7420746f6b656e00000000000000000000000000000060648201526084016104d6565b60006107ac83610b3c565b9050806040516020016107bf9190611346565b604051602081830303815290604052915050919050565b600754600160a060020a03163314610838576040516000805160206114ba833981519152815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657260448201526064016104d6565b600160a060020a0381166108bc576040516000805160206114ba833981519152815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201527f646472657373000000000000000000000000000000000000000000000000000060648201526084016104d6565b6108c581610add565b50565b600160a060020a038216610926576040516000805160206114ba833981519152815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f206164647265737360448201526064016104d6565b600081815260026020526040902054600160a060020a031615610993576040516000805160206114ba833981519152815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e7465640000000060448201526064016104d6565b600160a060020a03821660009081526003602052604081208054600192906109bc9084906113ba565b9091555050600081815260026020526040808220805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03861690811790915590518392907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b600082815260026020526040902054600160a060020a0316610ab9576040516000805160206114ba833981519152815260206004820152602e60248201527f45524337323155524953746f726167653a2055524920736574206f66206e6f6e60448201527f6578697374656e7420746f6b656e00000000000000000000000000000000000060648201526084016104d6565b60008281526006602090815260409091208251610ad892840190610f0f565b505050565b60078054600160a060020a0383811673ffffffffffffffffffffffffffffffffffffffff19831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b600081815260026020526040902054606090600160a060020a0316610bd1576040516000805160206114ba833981519152815260206004820152603160248201527f45524337323155524953746f726167653a2055524920717565727920666f722060448201527f6e6f6e6578697374656e7420746f6b656e00000000000000000000000000000060648201526084016104d6565b60008281526006602052604081208054610bea906112f2565b80601f0160208091040260200160405190810160405280929190818152602001828054610c16906112f2565b8015610c635780601f10610c3857610100808354040283529160200191610c63565b820191906000526020600020905b815481529060010190602001808311610c4657829003601f168201915b505050505090506000610c8160408051602081019091526000815290565b9050805160001415610c94575092915050565b815115610cc6578082604051602001610cae9291906113d2565b60405160208183030381529060405292505050919050565b610ccf84610cd7565b949350505050565b600081815260026020526040902054606090600160a060020a0316610d6c576040516000805160206114ba833981519152815260206004820152602f60248201527f4552433732314d657461646174613a2055524920717565727920666f72206e6f60448201527f6e6578697374656e7420746f6b656e000000000000000000000000000000000060648201526084016104d6565b6000610d8360408051602081019091526000815290565b90506000815111610da357604051806020016040528060008152506102d3565b80610dad84610dbe565b6040516020016107bf9291906113d2565b606081610dfe57505060408051808201909152600181527f3000000000000000000000000000000000000000000000000000000000000000602082015290565b8160005b8115610e285780610e1281611401565b9150610e219050600a8361144b565b9150610e02565b60008167ffffffffffffffff811115610e4357610e43610fc4565b6040519080825280601f01601f191660200182016040528015610e6d576020820181803683370190505b5090505b8415610ccf57610e8260018361145f565b9150610e8f600a86611476565b610e9a9060306113ba565b7f010000000000000000000000000000000000000000000000000000000000000002818381518110610ece57610ece61148a565b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350610f08600a8661144b565b9450610e71565b828054610f1b906112f2565b90600052602060002090601f016020900481019282610f3d5760008555610f83565b82601f10610f5657805160ff1916838001178555610f83565b82800160010185558215610f83579182015b82811115610f83578251825591602001919060010190610f68565b50610f8f929150610f93565b5090565b5b80821115610f8f5760008155600101610f94565b8035600160a060020a0381168114610fbf57600080fd5b919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b600067ffffffffffffffff8084111561100e5761100e610fc4565b604051601f8501601f19908116603f0116810190828211818310171561103657611036610fc4565b8160405280935085815286868601111561104f57600080fd5b858560208301376000602087830101525050509392505050565b6000806040838503121561107c57600080fd5b61108583610fa8565b9150602083013567ffffffffffffffff8111156110a157600080fd5b8301601f810185136110b257600080fd5b6110c185823560208401610ff3565b9150509250929050565b6000602082840312156110dd57600080fd5b81357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19811681146102d357600080fd5b60005b8381101561112557818101518382015260200161110d565b83811115611134576000848401525b50505050565b602081526000825180602084015261115981604085016020870161110a565b601f01601f19169190910160400192915050565b60006020828403121561117f57600080fd5b5035919050565b6000806040838503121561119957600080fd5b6111a283610fa8565b946020939093013593505050565b6000806000606084860312156111c557600080fd5b6111ce84610fa8565b92506111dc60208501610fa8565b9150604084013590509250925092565b6000602082840312156111fe57600080fd5b6102d382610fa8565b6000806040838503121561121a57600080fd5b61122383610fa8565b91506020830135801515811461123857600080fd5b809150509250929050565b6000806000806080858703121561125957600080fd5b61126285610fa8565b935061127060208601610fa8565b925060408501359150606085013567ffffffffffffffff81111561129357600080fd5b8501601f810187136112a457600080fd5b6112b387823560208401610ff3565b91505092959194509250565b600080604083850312156112d257600080fd5b6112db83610fa8565b91506112e960208401610fa8565b90509250929050565b60028104600182168061130657607f821691505b60208210811415611340577f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b50919050565b7f646174613a6170706c69636174696f6e2f6a736f6e3b6261736536342c00000081526000825161137e81601d85016020870161110a565b91909101601d0192915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600082198211156113cd576113cd61138b565b500190565b600083516113e481846020880161110a565b8351908301906113f881836020880161110a565b01949350505050565b60006000198214156114155761141561138b565b5060010190565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b60008261145a5761145a61141c565b500490565b6000828210156114715761147161138b565b500390565b6000826114855761148561141c565b500690565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fdfe08c379a000000000000000000000000000000000000000000000000000000000a26469706673582212201673f141db9f2351dfb7f39f24191c96f7304ed83711a7cfc3c85ca974b3ecbb64736f6c63430008090033"
        const val FUNC_APPROVE = "approve"
        const val FUNC_BALANCEOF = "balanceOf"
        const val FUNC_GETAPPROVED = "getApproved"
        const val FUNC_ISAPPROVEDFORALL = "isApprovedForAll"
        const val FUNC_MINTTO = "mintTo"
        const val FUNC_NAME = "name"
        const val FUNC_OWNER = "owner"
        const val FUNC_OWNEROF = "ownerOf"
        const val FUNC_RENOUNCEOWNERSHIP = "renounceOwnership"
        const val FUNC_safeTransferFrom = "safeTransferFrom"
        const val FUNC_SETAPPROVALFORALL = "setApprovalForAll"
        const val FUNC_SUPPORTSINTERFACE = "supportsInterface"
        const val FUNC_SYMBOL = "symbol"
        const val FUNC_TOKENURI = "tokenURI"
        const val FUNC_TRANSFERFROM = "transferFrom"
        const val FUNC_TRANSFEROWNERSHIP = "transferOwnership"
        val APPROVAL_EVENT = Event(
            "Approval",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>(true) {})
        )
        val APPROVALFORALL_EVENT = Event(
            "ApprovalForAll",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Bool?>() {})
        )
        val OWNERSHIPTRANSFERRED_EVENT = Event(
            "OwnershipTransferred",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {})
        )
        val TRANSFER_EVENT = Event(
            "Transfer",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>(true) {})
        )

        @Deprecated("")
        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            credentials: Credentials?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): Erc721OnchainCredentialWrapper {
            return Erc721OnchainCredentialWrapper(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): Erc721OnchainCredentialWrapper {
            return Erc721OnchainCredentialWrapper(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            credentials: Credentials?,
            contractGasProvider: ContractGasProvider?
        ): Erc721OnchainCredentialWrapper {
            return Erc721OnchainCredentialWrapper(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            contractGasProvider: ContractGasProvider?
        ): Erc721OnchainCredentialWrapper {
            return Erc721OnchainCredentialWrapper(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(
            web3j: Web3j?,
            credentials: Credentials?,
            contractGasProvider: ContractGasProvider?
        ): RemoteCall<Erc721OnchainCredentialWrapper> {
            return deployRemoteCall(
                Erc721OnchainCredentialWrapper::class.java,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                ""
            )
        }

        fun deploy(
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            contractGasProvider: ContractGasProvider?
        ): RemoteCall<Erc721OnchainCredentialWrapper> {
            return deployRemoteCall(
                Erc721OnchainCredentialWrapper::class.java,
                web3j,
                transactionManager,
                contractGasProvider,
                BINARY,
                ""
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j?,
            credentials: Credentials?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): RemoteCall<Erc721OnchainCredentialWrapper> {
            return deployRemoteCall(
                Erc721OnchainCredentialWrapper::class.java,
                web3j,
                credentials,
                gasPrice,
                gasLimit,
                BINARY,
                ""
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): RemoteCall<Erc721OnchainCredentialWrapper> {
            return deployRemoteCall(
                Erc721OnchainCredentialWrapper::class.java,
                web3j,
                transactionManager,
                gasPrice,
                gasLimit,
                BINARY,
                ""
            )
        }
    }
}
