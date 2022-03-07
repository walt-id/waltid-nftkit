package id.walt.nftkit.metadata

import id.walt.nftkit.services.NftMetadata

interface MetadataUri {

    fun getTokenUri(nftMetadata: NftMetadata): String
}