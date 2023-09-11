package id.walt.nftkit.opa

data class DynamicPolicyArg(
    val name: String = "DynamicPolicy",
    val description: String? = null,
    val input: Map<String, Any?>,
    val policy: String,
    //val dataPath: String = "\$.credentialSubject", // for specifying the input data
    val policyQuery: String = "data.system.main", // for evaluating the result from the rego engine
    val policyEngine: PolicyEngineType = PolicyEngineType.OPA,
)
