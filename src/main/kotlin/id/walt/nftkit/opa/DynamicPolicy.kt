package id.walt.nftkit.opa

import id.walt.nftkit.services.NftMetadata
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

        return false
    }

    private fun getNftMetadataValues(nftMetadata: NftMetadataWrapper): MutableMap<String, String?> {
        val data = mutableMapOf<String, String?>()
        if(nftMetadata.evmNftMetadata != null){
            data.put("name", nftMetadata.evmNftMetadata.name)
            data.put("description", nftMetadata.evmNftMetadata.description)
            nftMetadata.evmNftMetadata.attributes?.forEach { data.put(it.trait_type, it.value) }
        }else if(nftMetadata.tezosNftMetadata != null){
            data.put("name", nftMetadata.tezosNftMetadata.name)
            data.put("description", nftMetadata.tezosNftMetadata.description)
            data.put("symbol", nftMetadata.tezosNftMetadata.symbol)
            data.put("decimals", nftMetadata.tezosNftMetadata.decimals)
            data.put("displayUri", nftMetadata.tezosNftMetadata.displayUri)
            data.put("artifactUri", nftMetadata.tezosNftMetadata.name)
            data.put("thumbnailUri", nftMetadata.tezosNftMetadata.thumbnailUri)
            data.put("isTransferable", nftMetadata.tezosNftMetadata.isTransferable.toString())
            data.put("isBooleanAmount", nftMetadata.tezosNftMetadata.isBooleanAmount.toString())
            data.put("shouldPreferSymbol", nftMetadata.tezosNftMetadata.shouldPreferSymbol.toString())
            data.put("category", nftMetadata.tezosNftMetadata.category)
            data.put("collectionName", nftMetadata.tezosNftMetadata.collectionName)
            data.put("creatorName", nftMetadata.tezosNftMetadata.creatorName)
            nftMetadata.tezosNftMetadata.attributes?.forEach { data.put(it.name, it.value) }
        }else if(nftMetadata.nearNftMetadata != null){
            data.put("title", nftMetadata.nearNftMetadata.metadata.title)
            data.put("description", nftMetadata.nearNftMetadata.metadata.description)
            data.put("media", nftMetadata.nearNftMetadata.metadata.media)
            data.put("mediaHash", nftMetadata.nearNftMetadata.metadata.media_hash)
            data.put("copies", nftMetadata.nearNftMetadata.metadata.copies.toString())
            data.put("issuedAt", nftMetadata.nearNftMetadata.metadata.issued_at.toString())
            data.put("expiresAt", nftMetadata.nearNftMetadata.metadata.expires_at.toString())
            data.put("startsAt", nftMetadata.nearNftMetadata.metadata.starts_at.toString())
            data.put("updatedAt", nftMetadata.nearNftMetadata.metadata.updated_at.toString())
            data.put("reference", nftMetadata.nearNftMetadata.metadata.starts_at.toString())
            data.put("referenceHash", nftMetadata.nearNftMetadata.metadata.reference_hash.toString())
            data.put("extra", nftMetadata.nearNftMetadata.metadata.extra.toString())
            data.put("royalties", nftMetadata.nearNftMetadata.royalty.toString())
            data.put("approved_accounts", nftMetadata.nearNftMetadata.approved_account_ids.toString())


        }
        return data
    }
}

