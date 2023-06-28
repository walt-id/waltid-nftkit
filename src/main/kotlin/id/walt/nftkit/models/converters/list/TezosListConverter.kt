package id.walt.nftkit.models.converters.list

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftListConverterBase
import id.walt.nftkit.models.NftListDataTransferObject
import id.walt.nftkit.services.TezosNFTsTzktResult

class TezosListConverter(
    private val detailConverter: NftDetailConverterBase<TezosNFTsTzktResult>,
) : NftListConverterBase<List<TezosNFTsTzktResult>>() {
    override fun convert(data: List<TezosNFTsTzktResult>): NftListDataTransferObject = data.map {
        detailConverter.convert(it)
    }.let { NftListDataTransferObject(it) }
}