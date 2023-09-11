package id.walt.nftkit.opa

object PolicyRegistry {
    private var _policies: LinkedHashMap<String, DynamicPolicyArg>? = null
    private val policies: LinkedHashMap<String, DynamicPolicyArg>
        get() {
            if(_policies == null) {
                initPolicies()
            }
            return _policies!!
        }


    fun contains(id: String) = policies.containsKey(id)
    fun listPolicies() = policies


    fun createSavedPolicy(name: String, dynPolArg: DynamicPolicyArg): Boolean {
        if(!contains(name)) {
            policies[name] = dynPolArg
            return true
        }
        return false
    }

    private fun initPolicies() {
        _policies = linkedMapOf()
    }


}
