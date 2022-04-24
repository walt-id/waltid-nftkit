package id.walt.nftkit.metadata

object MetadataUriFactory {

    fun getMetadataUri(): MetadataUri {
        return OnChainMetadata()
    }
}
