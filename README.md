# NFT Kit
The **NFT Kit** by **walt.id** is a Kotlin library for minting & managing NFTs.

The core services are in the scope of:
- Deploy NFTs smart contract to multiple chains.
- Mint new NFTs.
- Manage Non-fungible tokens(NFTs).

## Installation
Set properties in the configuration file.

For the providers: you can get them either from [Infura](https://infura.io/) or [Alchemy](https://www.alchemy.com/)

You can find your `privateKey` from your ethereum wallet like [MetaMask](https://metamask.io/).

For the block explorer api keys: you can get them from [Ethereum](https://etherscan.io/) and [Polygon](https://polygonscan.com/)  

    Providers:
        ethereum: 
        rinkeby: "https://rinkeby.infura.io/v3/<api-key>"
        ropsten:  
        polygon: 
        mumbai: "https://polygon-mumbai.g.alchemy.com/v2/<api-key>"
    privateKey:"prv-key"
    blockExplorerScanApiKeys:
        ethereum: "<api-key>"
        polygon:  "<api-key>"


## Build

    docker build -t nftkit .


## RUN

     docker run -p 7000:7000 -it nftkit

### Use as JVM Depenceny

The required Maven repo is: https://maven.walt.id/repository/waltid/

Using Gradle:

    implementation("id.walt:waltid-nftkit:1.0.0")

or Maven:

    <dependency>
        <groupId>id.walt</groupId>
        <artifactId>waltid-nftkit</artifactId>
        <version>1.0.0</version>
    </dependency>


## Examples
Following code snipped gives a first impression how to use the NFT Kit for deploying **ERC721 smart contract**, for minting **new token** and for fetching **NFT metadata**.

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

## Documentation

The documentation is hosted at: https://docs.walt.id/v/nft-kit/

Direct links for using the NFT Kit are:

- Quick Start (Build & run the NFT Kit with Docker or natively): https://docs.walt.id/v/nft-kit/getting-started/quick-start
- Intro: https://docs.walt.id/v/nft-kit/what-is-the-nft-kit/readme
- APIs: https://docs.walt.id/v/nft-kit/getting-started/rest-apis

## License

```
Copyright ((C)) 2022 walt.id GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

