package id.walt.nftkit.models.converters.detail

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.models.TokenArt
import id.walt.nftkit.services.NFTsAlchemyResult

class EvmDetailConverter : NftDetailConverterBase<NFTsAlchemyResult.NftTokenByAlchemy>() {
    override fun convert(data: NFTsAlchemyResult.NftTokenByAlchemy): NftDetailDataTransferObject = NftDetailDataTransferObject(
        id = data.id.tokenId,
        name = data.title,
        contract = data.contract.address,
        description = data.description,
        type = data.id.tokenMetadata.tokenType,
        art = TokenArt(imageData = data.tokenUri.raw, url = data.tokenUri.gateway),
        externalUrl = "https://polygonscan.com/address/${data.contract.address}"
    )
}