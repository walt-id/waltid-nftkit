package id.walt.nftkit.models.converters.detail

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.models.TokenArt
import id.walt.nftkit.services.NearNftMetadata

class NearDetailConverter: NftDetailConverterBase<NearNftMetadata>() {
    override fun convert(data: NearNftMetadata): NftDetailDataTransferObject = NftDetailDataTransferObject(
        id = data.token_id,
        name = data.metadata.title,
//        contract = route.params.id.split(":")[0],TODO
        description = data.metadata.description,
        art = TokenArt(url = data.metadata.media),
    )
}