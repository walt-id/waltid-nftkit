package id.walt.nftkit.models.converters.list

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftListConverterBase
import id.walt.nftkit.models.NftListDataTransferObject
import id.walt.nftkit.services.NearNftMetadata

class NearListConverter(
    private val detailConverter: NftDetailConverterBase<NearNftMetadata>,
) : NftListConverterBase<List<NearNftMetadata>>() {
    override fun convert(data: List<NearNftMetadata>): NftListDataTransferObject = data.map {
        detailConverter.convert(it)
    }.let {
        NftListDataTransferObject(it)
    }
}