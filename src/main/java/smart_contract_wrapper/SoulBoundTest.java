package smart_contract_wrapper;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.4.
 */
@SuppressWarnings("rawtypes")
public class SoulBoundTest extends Contract {
    private static final String BINARY = "608060405234801562000010575f80fd5b506040518060400160405280600d81526020016c14dbdd5b109bdd5b9915195cdd609a1b8152506040518060400160405280600381526020016214d09560ea1b815250815f90816200006391906200018a565b5060016200007282826200018a565b5050506200008f620000896200009560201b60201c565b62000099565b62000252565b3390565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806200011357607f821691505b6020821081036200013257634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111562000185575f81815260208120601f850160051c81016020861015620001605750805b601f850160051c820191505b8181101562000181578281556001016200016c565b5050505b505050565b81516001600160401b03811115620001a657620001a6620000ea565b620001be81620001b78454620000fe565b8462000138565b602080601f831160018114620001f4575f8415620001dc5750858301515b5f19600386901b1c1916600185901b17855562000181565b5f85815260208120601f198616915b82811015620002245788860151825594840194600190910190840162000203565b50858210156200024257878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6117ad80620002605f395ff3fe608060405234801561000f575f80fd5b5060043610610106575f3560e01c8063715018a61161009e578063b88d4fde1161006e578063b88d4fde14610215578063c87b56dd14610228578063d204c45e1461023b578063e985e9c51461024e578063f2fde38b14610261575f80fd5b8063715018a6146101e15780638da5cb5b146101e957806395d89b41146101fa578063a22cb46514610202575f80fd5b806323b872dd116100d957806323b872dd1461018757806342842e0e1461019a5780636352211e146101ad57806370a08231146101c0575f80fd5b806301ffc9a71461010a57806306fdde0314610132578063081812fc14610147578063095ea7b314610172575b5f80fd5b61011d610118366004611185565b610274565b60405190151581526020015b60405180910390f35b61013a6102c5565b60405161012991906111ed565b61015a6101553660046111ff565b610354565b6040516001600160a01b039091168152602001610129565b610185610180366004611231565b610379565b005b610185610195366004611259565b610492565b6101856101a8366004611259565b6104c3565b61015a6101bb3660046111ff565b6104dd565b6101d36101ce366004611292565b61053c565b604051908152602001610129565b6101856105c0565b6007546001600160a01b031661015a565b61013a6105d3565b6101856102103660046112ab565b6105e2565b61018561022336600461136b565b6105f1565b61013a6102363660046111ff565b610629565b6101856102493660046113e2565b610634565b61011d61025c366004611440565b61066a565b61018561026f366004611292565b610697565b5f6001600160e01b031982166380ac58cd60e01b14806102a457506001600160e01b03198216635b5e139f60e01b145b806102bf57506301ffc9a760e01b6001600160e01b03198316145b92915050565b60605f80546102d390611471565b80601f01602080910402602001604051908101604052809291908181526020018280546102ff90611471565b801561034a5780601f106103215761010080835404028352916020019161034a565b820191905f5260205f20905b81548152906001019060200180831161032d57829003601f168201915b5050505050905090565b5f61035e82610710565b505f908152600460205260409020546001600160a01b031690565b5f610383826104dd565b9050806001600160a01b0316836001600160a01b0316036103f55760405162461bcd60e51b815260206004820152602160248201527f4552433732313a20617070726f76616c20746f2063757272656e74206f776e656044820152603960f91b60648201526084015b60405180910390fd5b336001600160a01b03821614806104115750610411813361066a565b6104835760405162461bcd60e51b815260206004820152603e60248201527f4552433732313a20617070726f76652063616c6c6572206973206e6f7420746f60448201527f6b656e206f776e6572206e6f7220617070726f76656420666f7220616c6c000060648201526084016103ec565b61048d838361076e565b505050565b61049c33826107db565b6104b85760405162461bcd60e51b81526004016103ec906114a9565b61048d838383610839565b61048d83838360405180602001604052805f8152506105f1565b5f818152600260205260408120546001600160a01b0316806102bf5760405162461bcd60e51b8152602060048201526018602482015277115490cdcc8c4e881a5b9d985b1a59081d1bdad95b88125160421b60448201526064016103ec565b5f6001600160a01b0382166105a55760405162461bcd60e51b815260206004820152602960248201527f4552433732313a2061646472657373207a65726f206973206e6f7420612076616044820152683634b21037bbb732b960b91b60648201526084016103ec565b506001600160a01b03165f9081526003602052604090205490565b6105c86109dc565b6105d15f610a36565b565b6060600180546102d390611471565b6105ed338383610a87565b5050565b6105fb33836107db565b6106175760405162461bcd60e51b81526004016103ec906114a9565b61062384848484610b54565b50505050565b60606102bf82610b87565b61063c6109dc565b5f61064660085490565b9050610656600880546001019055565b6106608382610c89565b61048d8183610ca2565b6001600160a01b039182165f90815260056020908152604080832093909416825291909152205460ff1690565b61069f6109dc565b6001600160a01b0381166107045760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b60648201526084016103ec565b61070d81610a36565b50565b5f818152600260205260409020546001600160a01b031661070d5760405162461bcd60e51b8152602060048201526018602482015277115490cdcc8c4e881a5b9d985b1a59081d1bdad95b88125160421b60448201526064016103ec565b5f81815260046020526040902080546001600160a01b0319166001600160a01b03841690811790915581906107a2826104dd565b6001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45050565b5f806107e6836104dd565b9050806001600160a01b0316846001600160a01b0316148061080d575061080d818561066a565b806108315750836001600160a01b031661082684610354565b6001600160a01b0316145b949350505050565b826001600160a01b031661084c826104dd565b6001600160a01b0316146108b05760405162461bcd60e51b815260206004820152602560248201527f4552433732313a207472616e736665722066726f6d20696e636f72726563742060448201526437bbb732b960d91b60648201526084016103ec565b6001600160a01b0382166109125760405162461bcd60e51b8152602060048201526024808201527f4552433732313a207472616e7366657220746f20746865207a65726f206164646044820152637265737360e01b60648201526084016103ec565b61091d838383610d33565b6109275f8261076e565b6001600160a01b0383165f90815260036020526040812080546001929061094f90849061150b565b90915550506001600160a01b0382165f90815260036020526040812080546001929061097c90849061151e565b90915550505f8181526002602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b6007546001600160a01b031633146105d15760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657260448201526064016103ec565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b816001600160a01b0316836001600160a01b031603610ae85760405162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c65720000000000000060448201526064016103ec565b6001600160a01b038381165f81815260056020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b610b5f848484610839565b610b6b84848484610d8a565b6106235760405162461bcd60e51b81526004016103ec90611531565b6060610b9282610710565b5f8281526006602052604081208054610baa90611471565b80601f0160208091040260200160405190810160405280929190818152602001828054610bd690611471565b8015610c215780601f10610bf857610100808354040283529160200191610c21565b820191905f5260205f20905b815481529060010190602001808311610c0457829003601f168201915b505050505090505f610c3d60408051602081019091525f815290565b905080515f03610c4e575092915050565b815115610c80578082604051602001610c68929190611583565b60405160208183030381529060405292505050919050565b61083184610e87565b6105ed828260405180602001604052805f815250610ef7565b5f828152600260205260409020546001600160a01b0316610d1c5760405162461bcd60e51b815260206004820152602e60248201527f45524337323155524953746f726167653a2055524920736574206f66206e6f6e60448201526d32bc34b9ba32b73a103a37b5b2b760911b60648201526084016103ec565b5f82815260066020526040902061048d82826115fe565b6001600160a01b0383161561048d5760405162461bcd60e51b815260206004820152601e60248201527f4572723a20746f6b656e207472616e7366657220697320424c4f434b4544000060448201526064016103ec565b5f6001600160a01b0384163b15610e7c57604051630a85bd0160e11b81526001600160a01b0385169063150b7a0290610dcd9033908990889088906004016116ba565b6020604051808303815f875af1925050508015610e07575060408051601f3d908101601f19168201909252610e04918101906116f6565b60015b610e62573d808015610e34576040519150601f19603f3d011682016040523d82523d5f602084013e610e39565b606091505b5080515f03610e5a5760405162461bcd60e51b81526004016103ec90611531565b805181602001fd5b6001600160e01b031916630a85bd0160e11b149050610831565b506001949350505050565b6060610e9282610710565b5f610ea760408051602081019091525f815290565b90505f815111610ec55760405180602001604052805f815250610ef0565b80610ecf84610f29565b604051602001610ee0929190611583565b6040516020818303038152906040525b9392505050565b610f018383611026565b610f0d5f848484610d8a565b61048d5760405162461bcd60e51b81526004016103ec90611531565b6060815f03610f4f5750506040805180820190915260018152600360fc1b602082015290565b815f5b8115610f785780610f6281611711565b9150610f719050600a8361173d565b9150610f52565b5f8167ffffffffffffffff811115610f9257610f926112e4565b6040519080825280601f01601f191660200182016040528015610fbc576020820181803683370190505b5090505b841561083157610fd160018361150b565b9150610fde600a86611750565b610fe990603061151e565b60f81b818381518110610ffe57610ffe611763565b60200101906001600160f81b03191690815f1a90535061101f600a8661173d565b9450610fc0565b6001600160a01b03821661107c5760405162461bcd60e51b815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f206164647265737360448201526064016103ec565b5f818152600260205260409020546001600160a01b0316156110e05760405162461bcd60e51b815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e7465640000000060448201526064016103ec565b6110eb5f8383610d33565b6001600160a01b0382165f90815260036020526040812080546001929061111390849061151e565b90915550505f8181526002602052604080822080546001600160a01b0319166001600160a01b03861690811790915590518392907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b6001600160e01b03198116811461070d575f80fd5b5f60208284031215611195575f80fd5b8135610ef081611170565b5f5b838110156111ba5781810151838201526020016111a2565b50505f910152565b5f81518084526111d98160208601602086016111a0565b601f01601f19169290920160200192915050565b602081525f610ef060208301846111c2565b5f6020828403121561120f575f80fd5b5035919050565b80356001600160a01b038116811461122c575f80fd5b919050565b5f8060408385031215611242575f80fd5b61124b83611216565b946020939093013593505050565b5f805f6060848603121561126b575f80fd5b61127484611216565b925061128260208501611216565b9150604084013590509250925092565b5f602082840312156112a2575f80fd5b610ef082611216565b5f80604083850312156112bc575f80fd5b6112c583611216565b9150602083013580151581146112d9575f80fd5b809150509250929050565b634e487b7160e01b5f52604160045260245ffd5b5f67ffffffffffffffff80841115611312576113126112e4565b604051601f8501601f19908116603f0116810190828211818310171561133a5761133a6112e4565b81604052809350858152868686011115611352575f80fd5b858560208301375f602087830101525050509392505050565b5f805f806080858703121561137e575f80fd5b61138785611216565b935061139560208601611216565b925060408501359150606085013567ffffffffffffffff8111156113b7575f80fd5b8501601f810187136113c7575f80fd5b6113d6878235602084016112f8565b91505092959194509250565b5f80604083850312156113f3575f80fd5b6113fc83611216565b9150602083013567ffffffffffffffff811115611417575f80fd5b8301601f81018513611427575f80fd5b611436858235602084016112f8565b9150509250929050565b5f8060408385031215611451575f80fd5b61145a83611216565b915061146860208401611216565b90509250929050565b600181811c9082168061148557607f821691505b6020821081036114a357634e487b7160e01b5f52602260045260245ffd5b50919050565b6020808252602e908201527f4552433732313a2063616c6c6572206973206e6f7420746f6b656e206f776e6560408201526d1c881b9bdc88185c1c1c9bdd995960921b606082015260800190565b634e487b7160e01b5f52601160045260245ffd5b818103818111156102bf576102bf6114f7565b808201808211156102bf576102bf6114f7565b60208082526032908201527f4552433732313a207472616e7366657220746f206e6f6e20455243373231526560408201527131b2b4bb32b91034b6b83632b6b2b73a32b960711b606082015260800190565b5f83516115948184602088016111a0565b8351908301906115a88183602088016111a0565b01949350505050565b601f82111561048d575f81815260208120601f850160051c810160208610156115d75750805b601f850160051c820191505b818110156115f6578281556001016115e3565b505050505050565b815167ffffffffffffffff811115611618576116186112e4565b61162c816116268454611471565b846115b1565b602080601f83116001811461165f575f84156116485750858301515b5f19600386901b1c1916600185901b1785556115f6565b5f85815260208120601f198616915b8281101561168d5788860151825594840194600190910190840161166e565b50858210156116aa57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6001600160a01b03858116825284166020820152604081018390526080606082018190525f906116ec908301846111c2565b9695505050505050565b5f60208284031215611706575f80fd5b8151610ef081611170565b5f60018201611722576117226114f7565b5060010190565b634e487b7160e01b5f52601260045260245ffd5b5f8261174b5761174b611729565b500490565b5f8261175e5761175e611729565b500690565b634e487b7160e01b5f52603260045260245ffdfea26469706673582212201a313239ed249fe011c5a670aad70e54a79928f56a2970dbe03081a0d8b2b2ab64736f6c63430008150033";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SAFEMINT = "safeMint";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected SoulBoundTest(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SoulBoundTest(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SoulBoundTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SoulBoundTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalForAllEventResponse>() {
            @Override
            public ApprovalForAllEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
                ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String to, BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Uint256> balanceOf(Address owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
                Arrays.asList(owner),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }
    public RemoteFunctionCall<TransactionReceipt> getApproved(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETAPPROVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> isApprovedForAll(String owner, String operator) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ISAPPROVEDFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.Address(160, operator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Utf8String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Address> ownerOf(Uint256 tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNEROF,
                Arrays.asList(tokenId),
                Arrays.asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeMint(String to, String uri) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SAFEMINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.Utf8String(uri)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }


    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId, byte[] data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(String operator, Boolean approved) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, operator), 
                new org.web3j.abi.datatypes.Bool(approved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Bool> supportsInterface(Bytes4 interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE,
                Arrays.asList(interfaceId),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<Utf8String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }



    public RemoteFunctionCall<Utf8String> tokenURI(Uint256 tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENURI,
                Arrays.asList(tokenId),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SoulBoundTest load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SoulBoundTest(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SoulBoundTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SoulBoundTest(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SoulBoundTest load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SoulBoundTest(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SoulBoundTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SoulBoundTest(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SoulBoundTest> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SoulBoundTest.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SoulBoundTest> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SoulBoundTest.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SoulBoundTest> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SoulBoundTest.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SoulBoundTest> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SoulBoundTest.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String approved;

        public BigInteger tokenId;
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public String owner;

        public String operator;

        public Boolean approved;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger tokenId;
    }
}
