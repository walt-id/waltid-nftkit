package id.walt.nftkit.opa

import id.walt.nftkit.services.NftMetadataWrapper

object DynamicPolicy {

    fun doVerify(input: Map<String, Any?>, policy: String, query: String, nftMetadata: NftMetadataWrapper): Boolean {
        val data = getNftMetadataValues(nftMetadata)
        return PolicyEngine.get(PolicyEngineType.OPA).validate(
            input = input,
            data = data,
            policy = policy,
            query = query
        )
    }

    private fun getNftMetadataValues(nftMetadata: NftMetadataWrapper): Map<String, String?> {
        val data: Map<String, String?> = when {
            nftMetadata.evmNftMetadata != null -> nftMetadata.evmNftMetadata.run {
                mapOf(
                    "name" to name,
                    "description" to description,
                    *((attributes?.map { it.trait_type to it.value.toString() }) ?: emptyList()).toTypedArray()
                )
            }

            nftMetadata.tezosNftMetadata != null -> nftMetadata.tezosNftMetadata.run {
                mapOf(
                    "name" to name,
                    "description" to description,
                    "symbol" to symbol,
                    "decimals" to decimals,
                    "displayUri" to displayUri,
                    "artifactUri" to name,
                    "thumbnailUri" to thumbnailUri,
                    "isTransferable" to isTransferable.toString(),
                    "isBooleanAmount" to isBooleanAmount.toString(),
                    "shouldPreferSymbol" to shouldPreferSymbol.toString(),
                    "category" to category,
                    "collectionName" to collectionName,
                    "creatorName" to creatorName,
                    *((attributes?.map { it.name to it.value }) ?: emptyList()).toTypedArray()
                )
            }

            nftMetadata.nearNftMetadata != null -> nftMetadata.nearNftMetadata.run {
                mapOf(
                    "title" to metadata.title,
                    "description" to metadata.description,
                    "media" to metadata.media,
                    "mediaHash" to metadata.media_hash,
                    "copies" to metadata.copies.toString(),
                    "issuedAt" to metadata.issued_at.toString(),
                    "expiresAt" to metadata.expires_at.toString(),
                    "startsAt" to metadata.starts_at.toString(),
                    "updatedAt" to metadata.updated_at.toString(),
                    "reference" to metadata.starts_at.toString(),
                    "referenceHash" to metadata.reference_hash.toString(),
                    "extra" to metadata.extra.toString(),
                    "royalties" to royalty.toString(),
                    "approved_accounts" to approved_account_ids.toString()
                )
            }

            nftMetadata.flowNftMetadata != null -> nftMetadata.flowNftMetadata.run {
                mapOf(
                    "name" to name,
                    "description" to description,
                    "media" to thumbnail,
                    "owner" to owner,
                    "externalURL" to externalURL.toString(),
                    "collectionName" to collectionName,
                    "traits" to traits?.traits.toString(),
                    "royalties" to royalties.toString()
                )
            }

            nftMetadata.uniqueNftMetadata != null -> nftMetadata.uniqueNftMetadata.run {
                mapOf(
                    "image" to ipfsCid,
                    *((attributes?.map { it.name to it.value.toString() }) ?: emptyList()).toTypedArray()
                )
            }

            nftMetadata.algorandNftMetadata != null -> nftMetadata.algorandNftMetadata.run {
                mapOf(
                    "name" to name,
                    "description" to description,
                    "image" to image,
                    "unitname" to unitName,
                )
            }

            else -> throw IllegalArgumentException("No NFT metadata or NFT metadata not supported: $nftMetadata")
        }
        return data
    }
}

