package id.walt.nftkit.metadata

import id.walt.nftkit.services.NftMetadataWrapper

interface MetadataUri {

    fun getTokenUri(nftMetadataWrapper: NftMetadataWrapper): String
}