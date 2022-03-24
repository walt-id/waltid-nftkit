package id.walt.nftkit.chains.evm.erc721

import id.walt.nftkit.Values
import id.walt.nftkit.WaltIdGasProvider
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.DeploymentResponse
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.services.WaltIdServices
import id.walt.nftkit.smart_contract_wrapper.ERC721URIStorage
import id.walt.nftkit.utilis.providers.ProviderFactory
import org.web3j.abi.datatypes.Address
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
import java.math.BigInteger


object Erc721TokenStandard : IErc721TokenStandard {


    fun deployContract(chain: Chain, name: String, symbol: String): DeploymentResponse {
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()
        val credentials: Credentials = Credentials.create("bd4cb3e507f342ee3a710370cef39dda48f17b0a158b0b8dd3f000fbd5b2c2d9")
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
            remotCall = ERC721URIStorage.deploy(web3j,transactionManager,gasProvider,Utf8String(name),Utf8String(symbol))
        }else{
            remotCall = ERC721URIStorage.deploy(web3j,credentials,gasProvider,Utf8String(name),Utf8String(symbol))
        }
        val contract= remotCall.send()

        val url = WaltIdServices.getBlockExplorerUrl(chain)
        val ts = TransactionResponse(contract.transactionReceipt.get().transactionHash,  "$url/tx/${contract.transactionReceipt.get().transactionHash}" )
        return DeploymentResponse(ts, contract.contractAddress, "$url/address/${contract.contractAddress}" )

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
        val web3j = ProviderFactory.getProvider(chain)?.getWeb3j()

        //val credentials: Credentials = Credentials.create("d720ef2cb49c6cbe94175ed413d27e635c5acaa1b7cf03d1faad3a0abc2f53f3")
        val credentials: Credentials = Credentials.create("bd4cb3e507f342ee3a710370cef39dda48f17b0a158b0b8dd3f000fbd5b2c2d9")

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
             return  ERC721URIStorage.load(address, web3j,transactionManager,gasProvider)
        }else{
            return ERC721URIStorage.load(address, web3j,credentials,gasProvider)
        }
    }


}