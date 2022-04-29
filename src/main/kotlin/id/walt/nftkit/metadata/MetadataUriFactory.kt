package id.walt.nftkit.metadata

import id.walt.nftkit.services.MetadataStorageType

object MetadataUriFactory {

    fun getMetadataUri(metadataStorageType: MetadataStorageType): MetadataUri {
            return when(metadataStorageType){
                MetadataStorageType.ON_CHAIN -> OnChainMetadata
                MetadataStorageType.OFF_CHAIN -> IPFSMetadata
            }
    }
}
