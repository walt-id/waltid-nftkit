package id.walt.auditor.dynamic

import id.walt.nftkit.services.NftMetadata

object DynamicPolicy {

     fun doVerify(input: Map<String, Any?>, policy: String, query: String, nftMetadata: NftMetadata): Boolean {
         val data = mutableMapOf("name" to nftMetadata.name, "description" to nftMetadata.description)
         nftMetadata.attributes?.forEach { data.put(it.trait_type, it.value) }
         return PolicyEngine.get(PolicyEngineType.OPA).validate(
            input = input,
            data = data,
            policy = policy,
            query = query
        )
        return false
    }

}

