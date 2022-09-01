<div align="center">
 <h1>NFT Kit</h1>
 <span>by </span><a href="https://walt.id">walt.id</a>
 <p>Use digital assets like non-fungible tokens (NFTs)<p>

<a href="https://walt.id/community">
<img src="https://img.shields.io/badge/Join-The Community-blue.svg?style=flat" alt="Join community!" />
</a>
<a href="https://twitter.com/intent/follow?screen_name=walt_id">
<img src="https://img.shields.io/twitter/follow/walt_id.svg?label=Follow%20@walt_id" alt="Follow @walt_id" />
</a>

</div>

## Getting Started

- [REST Api](https://docs.walt.id/v/nft-kit/getting-started/rest-apis) - Use the functionality of the NFT Kit via an REST api.
- [Maven/Gradle Dependency](https://docs.walt.id/v/nft-kit/getting-started/dependency-jvm) - Use the functions of the NFT Kit in a Kotlin/Java project.

Checkout the [Official Documentation](https://docs.walt.id/v/nft-kit/nft-kit/readme), to find out more.

## What is the NFT Kit?
A Kotlin library for managing NFTs 

### Features
- Deployment and Access Management of NFT smart contracts on multiple chains
- Base functionalities like minting NFTs, managing NFT metadata and much more
- Verification of NFT ownership and token traits
- Smart Contract extensions to add for example token transfer control


## Example
- Deployment of ERC721 smart contract
- Minting of a new token
- Fetching of NFT metadata

```kotlin
fun main() {
    
        // Deploy new ERC721 smart contract instance on polygon network
        val deploymentOptions = DeploymentOptions(AccessControl.OWNABLE, TokenStandard.ERC721)
        val deploymentParameter = DeploymentParameter("Metaverse", "MTV",DeploymentParameter.Options(true, true))
        val result = NftService.deploySmartContractToken(Chain.POLYGON, deploymentParameter, deploymentOptions)
    
        // Mint new NFT
        val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "trait_type1", value = "value1")
        val attribute2 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "trait_type2", value = "value2")
        val attributes = mutableListOf(attribute1, attribute2)
        val nftMetadata : NftMetadata = NftMetadata(name = "name", description = "description", image = "", attributes = attributes)
        val mintingParameter = MintingParameter("", "0xaf87c5Ce7a1fb6BD5aaDB6dd9C0b8EF51EF1BC31",nftMetadata)
        val mintingOptions = MintingOptions(MetadataStorageType.ON_CHAIN)
        val result = NftService.mintToken(Chain.POLYGON,"0xFd9426f82Ae1edBC6b5eC2B0Ea5416D34Ca6E9b6", mintingParameter, mintingOptions)       

        // Fetch NFT Metadata URI 
        val result = NftService.getNftMetadataUri(Chain.POLYGON, "0xFd9426f82Ae1edBC6b5eC2B0Ea5416D34Ca6E9b6", BigInteger.valueOf(26))

        // Fetch NFT Metadata
        val result = NftService.getNftMetadata(Chain.POLYGON, "0xFd9426f82Ae1edBC6b5eC2B0Ea5416D34Ca6E9b6", BigInteger.valueOf(26))
    }
```

## Join the community

* Connect and get the latest updates: [Discord](https://discord.gg/AW8AgqJthZ) | [Newsletter](https://walt.id/newsletter) | [YouTube](https://www.youtube.com/channel/UCXfOzrv3PIvmur_CmwwmdLA) | [Twitter](https://mobile.twitter.com/walt_id)
* Get help, request features and report bugs: [GitHub Discussions](https://github.com/walt-id/.github/discussions)

## License

Licensed under the [Apache License, Version 2.0](https://github.com/walt-id/waltid-nftkit/blob/main/LICENSE)

