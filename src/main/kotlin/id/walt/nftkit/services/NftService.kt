package id.walt.nftkit.services



data class NftMetadata(
    val description: String,
    val name: String,
    val attributes: List<Attributes>
) {
    data class Attributes(
        val name: String,
        val value: String
    )
}

data class MintingOptions(
    val onchain: Boolean = true,
    val storageType: Boolean = false
)

enum class Chain {
    ETHEREUM,
    PALYGON,
    BSC,
    RINKEBY,
}
enum class TokenStandard {
    ERC721,
    ERC1155
}



object NftService {

    fun DeployNewSmartContractToken(chain: Chain, tokenStandard: TokenStandard){}

    fun mintToken(contractAddress: String, chain: Chain, tokenStandard: TokenStandard, recipientAddress: String, metadata: NftMetadata, options: MintingOptions?) {}

    fun getNftMetadata(contractAddress: String, chain: Chain, tokenStandard: TokenStandard, tokenId: Int){}
}