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
        }
        return data
    }
}

