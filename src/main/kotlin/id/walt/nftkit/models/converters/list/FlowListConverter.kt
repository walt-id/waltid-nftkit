package id.walt.nftkit.models.converters.list

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftListConverterBase
import id.walt.nftkit.models.NftListDataTransferObject
import id.walt.nftkit.services.FlowNFTMetadata

class FlowListConverter(
    private val detailConverter: NftDetailConverterBase<FlowNFTMetadata>,
) : NftListConverterBase<List<FlowNFTMetadata>>() {
    override fun convert(data: List<FlowNFTMetadata>): NftListDataTransferObject = data.map {
        detailConverter.convert(it)
    }.let {
        NftListDataTransferObject(it)
    }
}