package id.walt.nftkit.models.converters.detail

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.models.TokenArt
import id.walt.nftkit.models.TokenAttributes
import id.walt.nftkit.services.FlowNFTMetadata

class FlowDetailConverter: NftDetailConverterBase<FlowNFTMetadata>() {
    override fun convert(data: FlowNFTMetadata): NftDetailDataTransferObject = NftDetailDataTransferObject(
        id = data.id,
        name = data.name,
//        contract = $auth.user.flowAccount :TODO,
        description = data.description,
        type = data.type,//??"FLIP-0636",
        art = TokenArt(url = data.thumbnail),
        externalUrl = data.externalURL,
        attributes = data.traits?.traits?.mapNotNull {
            it.takeIf { !it.name.isNullOrEmpty() && !it.value.isNullOrEmpty() }?.let {
                TokenAttributes(trait = it.name!!, value = it.value!!)
            }
        } ?: emptyList(),
    )
}