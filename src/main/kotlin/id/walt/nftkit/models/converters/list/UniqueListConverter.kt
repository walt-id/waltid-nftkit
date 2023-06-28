package id.walt.nftkit.models.converters.list

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftListConverterBase
import id.walt.nftkit.models.NftListDataTransferObject
import id.walt.nftkit.services.PolkadotUniqueNft

class UniqueListConverter(
    private val detailConverter: NftDetailConverterBase<PolkadotUniqueNft>,
) : NftListConverterBase<List<PolkadotUniqueNft>>() {
    override fun convert(data: List<PolkadotUniqueNft>): NftListDataTransferObject = data.map {
        detailConverter.convert(it)
    }.let { NftListDataTransferObject(it) }
}