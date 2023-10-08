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
public class WaltidSoulBound extends Contract {
    private static final String BINARY = "60806040523480156200001157600080fd5b506040518060400160405280600981526020016815d85b1d1a5914d09560ba1b8152506040518060400160405280600381526020016214d09560ea1b81525081600090816200006191906200018e565b5060016200007082826200018e565b5050506200008d620000876200009360201b60201c565b62000097565b6200025a565b3390565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200011457607f821691505b6020821081036200013557634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200018957600081815260208120601f850160051c81016020861015620001645750805b601f850160051c820191505b81811015620001855782815560010162000170565b5050505b505050565b81516001600160401b03811115620001aa57620001aa620000e9565b620001c281620001bb8454620000ff565b846200013b565b602080601f831160018114620001fa5760008415620001e15750858301515b600019600386901b1c1916600185901b17855562000185565b600085815260208120601f198616915b828110156200022b578886015182559484019460019091019084016200020a565b50858210156200024a5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b611a23806200026a6000396000f3fe608060405234801561001057600080fd5b50600436106101215760003560e01c8063715018a6116100ad578063c87b56dd11610071578063c87b56dd14610257578063d179e3d01461026a578063d204c45e1461027d578063e985e9c514610290578063f2fde38b146102a357600080fd5b8063715018a6146102105780638da5cb5b1461021857806395d89b4114610229578063a22cb46514610231578063b88d4fde1461024457600080fd5b806320c5429b116100f457806320c5429b146101a357806323b872dd146101b657806342842e0e146101c95780636352211e146101dc57806370a08231146101ef57600080fd5b806301ffc9a71461012657806306fdde031461014e578063081812fc14610163578063095ea7b31461018e575b600080fd5b6101396101343660046113c4565b6102b6565b60405190151581526020015b60405180910390f35b610156610308565b6040516101459190611431565b610176610171366004611444565b61039a565b6040516001600160a01b039091168152602001610145565b6101a161019c366004611479565b6103c1565b005b6101a16101b1366004611444565b6104db565b6101a16101c43660046114a3565b6104ef565b6101a16101d73660046114a3565b610520565b6101766101ea366004611444565b61053b565b6102026101fd3660046114df565b61059b565b604051908152602001610145565b6101a1610621565b6007546001600160a01b0316610176565b610156610635565b6101a161023f3660046114fa565b610644565b6101a16102523660046115c2565b610653565b610156610265366004611444565b61068b565b6101a1610278366004611444565b610696565b6101a161028b36600461163e565b610700565b61013961029e3660046116a0565b610737565b6101a16102b13660046114df565b610765565b60006001600160e01b031982166380ac58cd60e01b14806102e757506001600160e01b03198216635b5e139f60e01b145b8061030257506301ffc9a760e01b6001600160e01b03198316145b92915050565b606060008054610317906116d3565b80601f0160208091040260200160405190810160405280929190818152602001828054610343906116d3565b80156103905780601f1061036557610100808354040283529160200191610390565b820191906000526020600020905b81548152906001019060200180831161037357829003601f168201915b5050505050905090565b60006103a5826107db565b506000908152600460205260409020546001600160a01b031690565b60006103cc8261053b565b9050806001600160a01b0316836001600160a01b03160361043e5760405162461bcd60e51b815260206004820152602160248201527f4552433732313a20617070726f76616c20746f2063757272656e74206f776e656044820152603960f91b60648201526084015b60405180910390fd5b336001600160a01b038216148061045a575061045a8133610737565b6104cc5760405162461bcd60e51b815260206004820152603e60248201527f4552433732313a20617070726f76652063616c6c6572206973206e6f7420746f60448201527f6b656e206f776e6572206e6f7220617070726f76656420666f7220616c6c00006064820152608401610435565b6104d6838361083a565b505050565b6104e36108a8565b6104ec81610902565b50565b6104f9338261090b565b6105155760405162461bcd60e51b81526004016104359061170d565b6104d683838361096a565b6104d683838360405180602001604052806000815250610653565b6000818152600260205260408120546001600160a01b0316806103025760405162461bcd60e51b8152602060048201526018602482015277115490cdcc8c4e881a5b9d985b1a59081d1bdad95b88125160421b6044820152606401610435565b60006001600160a01b0382166106055760405162461bcd60e51b815260206004820152602960248201527f4552433732313a2061646472657373207a65726f206973206e6f7420612076616044820152683634b21037bbb732b960b91b6064820152608401610435565b506001600160a01b031660009081526003602052604090205490565b6106296108a8565b6106336000610b11565b565b606060018054610317906116d3565b61064f338383610b63565b5050565b61065d338361090b565b6106795760405162461bcd60e51b81526004016104359061170d565b61068584848484610c31565b50505050565b606061030282610c64565b336106a08261053b565b6001600160a01b0316146104e35760405162461bcd60e51b815260206004820152602160248201527f596f7520617265206e6f7420746865206f776e6572206f662074686973204e466044820152601560fa1b6064820152608401610435565b6107086108a8565b600061071360085490565b9050610723600880546001019055565b61072d8382610d6c565b6104d68183610d86565b6001600160a01b03918216600090815260056020908152604080832093909416825291909152205460ff1690565b61076d6108a8565b6001600160a01b0381166107d25760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152608401610435565b6104ec81610b11565b6000818152600260205260409020546001600160a01b03166104ec5760405162461bcd60e51b8152602060048201526018602482015277115490cdcc8c4e881a5b9d985b1a59081d1bdad95b88125160421b6044820152606401610435565b600081815260046020526040902080546001600160a01b0319166001600160a01b038416908117909155819061086f8261053b565b6001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45050565b6007546001600160a01b031633146106335760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152606401610435565b6104ec81610e19565b6000806109178361053b565b9050806001600160a01b0316846001600160a01b0316148061093e575061093e8185610737565b806109625750836001600160a01b03166109578461039a565b6001600160a01b0316145b949350505050565b826001600160a01b031661097d8261053b565b6001600160a01b0316146109e15760405162461bcd60e51b815260206004820152602560248201527f4552433732313a207472616e736665722066726f6d20696e636f72726563742060448201526437bbb732b960d91b6064820152608401610435565b6001600160a01b038216610a435760405162461bcd60e51b8152602060048201526024808201527f4552433732313a207472616e7366657220746f20746865207a65726f206164646044820152637265737360e01b6064820152608401610435565b610a4e838383610e59565b610a5960008261083a565b6001600160a01b0383166000908152600360205260408120805460019290610a82908490611771565b90915550506001600160a01b0382166000908152600360205260408120805460019290610ab0908490611784565b909155505060008181526002602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b816001600160a01b0316836001600160a01b031603610bc45760405162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c6572000000000000006044820152606401610435565b6001600160a01b03838116600081815260056020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b610c3c84848461096a565b610c4884848484610ec2565b6106855760405162461bcd60e51b815260040161043590611797565b6060610c6f826107db565b60008281526006602052604081208054610c88906116d3565b80601f0160208091040260200160405190810160405280929190818152602001828054610cb4906116d3565b8015610d015780601f10610cd657610100808354040283529160200191610d01565b820191906000526020600020905b815481529060010190602001808311610ce457829003601f168201915b505050505090506000610d1f60408051602081019091526000815290565b90508051600003610d31575092915050565b815115610d63578082604051602001610d4b9291906117e9565b60405160208183030381529060405292505050919050565b61096284610fc3565b61064f828260405180602001604052806000815250611037565b6000828152600260205260409020546001600160a01b0316610e015760405162461bcd60e51b815260206004820152602e60248201527f45524337323155524953746f726167653a2055524920736574206f66206e6f6e60448201526d32bc34b9ba32b73a103a37b5b2b760911b6064820152608401610435565b60008281526006602052604090206104d68282611866565b610e228161106a565b60008181526006602052604090208054610e3b906116d3565b1590506104ec5760008181526006602052604081206104ec91611360565b6001600160a01b0383161580610e7657506001600160a01b038216155b6104d65760405162461bcd60e51b815260206004820152601e60248201527f4572723a20746f6b656e207472616e7366657220697320424c4f434b454400006044820152606401610435565b60006001600160a01b0384163b15610fb857604051630a85bd0160e11b81526001600160a01b0385169063150b7a0290610f06903390899088908890600401611926565b6020604051808303816000875af1925050508015610f41575060408051601f3d908101601f19168201909252610f3e91810190611963565b60015b610f9e573d808015610f6f576040519150601f19603f3d011682016040523d82523d6000602084013e610f74565b606091505b508051600003610f965760405162461bcd60e51b815260040161043590611797565b805181602001fd5b6001600160e01b031916630a85bd0160e11b149050610962565b506001949350505050565b6060610fce826107db565b6000610fe560408051602081019091526000815290565b905060008151116110055760405180602001604052806000815250611030565b8061100f84611111565b6040516020016110209291906117e9565b6040516020818303038152906040525b9392505050565b6110418383611212565b61104e6000848484610ec2565b6104d65760405162461bcd60e51b815260040161043590611797565b60006110758261053b565b905061108381600084610e59565b61108e60008361083a565b6001600160a01b03811660009081526003602052604081208054600192906110b7908490611771565b909155505060008281526002602052604080822080546001600160a01b0319169055518391906001600160a01b038416907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908390a45050565b6060816000036111385750506040805180820190915260018152600360fc1b602082015290565b8160005b8115611162578061114c81611980565b915061115b9050600a836119af565b915061113c565b60008167ffffffffffffffff81111561117d5761117d611536565b6040519080825280601f01601f1916602001820160405280156111a7576020820181803683370190505b5090505b8415610962576111bc600183611771565b91506111c9600a866119c3565b6111d4906030611784565b60f81b8183815181106111e9576111e96119d7565b60200101906001600160f81b031916908160001a90535061120b600a866119af565b94506111ab565b6001600160a01b0382166112685760405162461bcd60e51b815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f20616464726573736044820152606401610435565b6000818152600260205260409020546001600160a01b0316156112cd5760405162461bcd60e51b815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e746564000000006044820152606401610435565b6112d960008383610e59565b6001600160a01b0382166000908152600360205260408120805460019290611302908490611784565b909155505060008181526002602052604080822080546001600160a01b0319166001600160a01b03861690811790915590518392907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b50805461136c906116d3565b6000825580601f1061137c575050565b601f0160209004906000526020600020908101906104ec91905b808211156113aa5760008155600101611396565b5090565b6001600160e01b0319811681146104ec57600080fd5b6000602082840312156113d657600080fd5b8135611030816113ae565b60005b838110156113fc5781810151838201526020016113e4565b50506000910152565b6000815180845261141d8160208601602086016113e1565b601f01601f19169290920160200192915050565b6020815260006110306020830184611405565b60006020828403121561145657600080fd5b5035919050565b80356001600160a01b038116811461147457600080fd5b919050565b6000806040838503121561148c57600080fd5b6114958361145d565b946020939093013593505050565b6000806000606084860312156114b857600080fd5b6114c18461145d565b92506114cf6020850161145d565b9150604084013590509250925092565b6000602082840312156114f157600080fd5b6110308261145d565b6000806040838503121561150d57600080fd5b6115168361145d565b91506020830135801515811461152b57600080fd5b809150509250929050565b634e487b7160e01b600052604160045260246000fd5b600067ffffffffffffffff8084111561156757611567611536565b604051601f8501601f19908116603f0116810190828211818310171561158f5761158f611536565b816040528093508581528686860111156115a857600080fd5b858560208301376000602087830101525050509392505050565b600080600080608085870312156115d857600080fd5b6115e18561145d565b93506115ef6020860161145d565b925060408501359150606085013567ffffffffffffffff81111561161257600080fd5b8501601f8101871361162357600080fd5b6116328782356020840161154c565b91505092959194509250565b6000806040838503121561165157600080fd5b61165a8361145d565b9150602083013567ffffffffffffffff81111561167657600080fd5b8301601f8101851361168757600080fd5b6116968582356020840161154c565b9150509250929050565b600080604083850312156116b357600080fd5b6116bc8361145d565b91506116ca6020840161145d565b90509250929050565b600181811c908216806116e757607f821691505b60208210810361170757634e487b7160e01b600052602260045260246000fd5b50919050565b6020808252602e908201527f4552433732313a2063616c6c6572206973206e6f7420746f6b656e206f776e6560408201526d1c881b9bdc88185c1c1c9bdd995960921b606082015260800190565b634e487b7160e01b600052601160045260246000fd5b818103818111156103025761030261175b565b808201808211156103025761030261175b565b60208082526032908201527f4552433732313a207472616e7366657220746f206e6f6e20455243373231526560408201527131b2b4bb32b91034b6b83632b6b2b73a32b960711b606082015260800190565b600083516117fb8184602088016113e1565b83519083019061180f8183602088016113e1565b01949350505050565b601f8211156104d657600081815260208120601f850160051c8101602086101561183f5750805b601f850160051c820191505b8181101561185e5782815560010161184b565b505050505050565b815167ffffffffffffffff81111561188057611880611536565b6118948161188e84546116d3565b84611818565b602080601f8311600181146118c957600084156118b15750858301515b600019600386901b1c1916600185901b17855561185e565b600085815260208120601f198616915b828110156118f8578886015182559484019460019091019084016118d9565b50858210156119165787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6001600160a01b038581168252841660208201526040810183905260806060820181905260009061195990830184611405565b9695505050505050565b60006020828403121561197557600080fd5b8151611030816113ae565b6000600182016119925761199261175b565b5060010190565b634e487b7160e01b600052601260045260246000fd5b6000826119be576119be611999565b500490565b6000826119d2576119d2611999565b500690565b634e487b7160e01b600052603260045260246000fdfea26469706673582212208de15e480afc15138ccd1a80baf875529a5d5098c1b72ef9accf739ef6c17a1664736f6c63430008130033";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REVOKE = "revoke";

    public static final String FUNC_SAFEMINT = "safeMint";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UNEQUIP = "unequip";

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
    protected WaltidSoulBound(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected WaltidSoulBound(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected WaltidSoulBound(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected WaltidSoulBound(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<TransactionReceipt> revoke(Uint256 tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REVOKE,
                Arrays.<Type>asList(tokenId),
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

    public RemoteFunctionCall<TransactionReceipt> unequip(Uint256 tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UNEQUIP, 
                Arrays.<Type>asList(tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static WaltidSoulBound load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new WaltidSoulBound(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static WaltidSoulBound load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new WaltidSoulBound(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static WaltidSoulBound load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new WaltidSoulBound(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static WaltidSoulBound load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WaltidSoulBound(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WaltidSoulBound> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WaltidSoulBound.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<WaltidSoulBound> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WaltidSoulBound.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WaltidSoulBound> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WaltidSoulBound.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WaltidSoulBound> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WaltidSoulBound.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
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
