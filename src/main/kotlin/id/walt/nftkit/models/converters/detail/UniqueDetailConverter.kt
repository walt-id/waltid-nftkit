package id.walt.nftkit.models.converters.detail

import id.walt.nftkit.models.NftDetailConverterBase
import id.walt.nftkit.models.NftDetailDataTransferObject
import id.walt.nftkit.models.TokenArt
import id.walt.nftkit.models.TokenAttributes
import id.walt.nftkit.services.PolkadotUniqueNft

class UniqueDetailConverter : NftDetailConverterBase<PolkadotUniqueNft>() {
    override fun convert(data: PolkadotUniqueNft): NftDetailDataTransferObject = NftDetailDataTransferObject(
            id = data.tokenId,
            art = TokenArt(url = data.metadata?.fullUrl),
            attributes = data.metadata?.attributes?.map {
                TokenAttributes(trait = it.name, value = it.value)
            } ?: emptyList(),
            //...TODO
        )
}