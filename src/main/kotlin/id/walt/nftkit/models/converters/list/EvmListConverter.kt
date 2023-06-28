package id.walt.nftkit.models.converters.list

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftListConverterBase
import id.walt.nftkit.models.NftListDataTransferObject
import id.walt.nftkit.services.NFTsAlchemyResult

class EvmListConverter(
    private val detailConverter: NftDetailConverterBase<NFTsAlchemyResult.NftTokenByAlchemy>,
) : NftListConverterBase<List<NFTsAlchemyResult.NftTokenByAlchemy>>() {
    override fun convert(data: List<NFTsAlchemyResult.NftTokenByAlchemy>): NftListDataTransferObject = data.map {
        detailConverter.convert(it)
    }.let { NftListDataTransferObject(it) }
}