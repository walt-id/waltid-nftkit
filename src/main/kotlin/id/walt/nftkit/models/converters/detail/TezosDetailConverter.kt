package id.walt.nftkit.models.converters.detail

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.models.TokenArt
import id.walt.nftkit.services.TezosNFTsTzktResult

class TezosDetailConverter: NftDetailConverterBase<TezosNFTsTzktResult>() {
    override fun convert(data: TezosNFTsTzktResult): NftDetailDataTransferObject = NftDetailDataTransferObject(
        id = data.token.tokenId,
        name = data.token.metadata?.name,
        contract = data.token.contract.address,
        description = data.token.metadata?.description,
        type = data.token.standard,
        art = TokenArt(data.token.metadata?.image ?: data.token.metadata?.displayUri),
        externalUrl = "https://tzkt.io/${data.token.contract.address}/operations"//TODO: "https://ghostnet.tzkt.io/${data.token.contract.address}/operations"
    )
}