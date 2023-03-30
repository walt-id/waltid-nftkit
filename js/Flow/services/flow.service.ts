import * as dotenv from "dotenv";

const fcl = require("@onflow/fcl");

dotenv.config();
class FlowService {

  async getAccountDetails() {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");

    return await fcl.account("0xa9ccb9756a0ee7eb")
  }


  async getAllNFTs(Address: string) {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    const ad = Address;

    console.log("this is the add", ad);
    const response = await fcl.query({
      cadence: `
   import MetadataViews from 0x631e88ae7f1d7c20
import NFTCatalog from 0x324c34e1c517e4db

import NFTRetrieval from 0x324c34e1c517e4db

pub struct NFT {
    pub let id: UInt64
    pub let name: String
    pub let description: String
    pub let thumbnail: String
    pub let externalURL: String
    pub let storagePath: StoragePath
    pub let publicPath: PublicPath
    pub let privatePath: PrivatePath
    pub let publicLinkedType: Type
    pub let privateLinkedType: Type
    pub let collectionName: String
    pub let collectionDescription: String
    pub let collectionSquareImage: String
    pub let collectionBannerImage: String
    pub let collectionExternalURL: String
    pub let royalties: [MetadataViews.Royalty]

    init(
        id: UInt64,
        name: String,
        description: String,
        thumbnail: String,
        externalURL: String,
        storagePath: StoragePath,
        publicPath: PublicPath,
        privatePath: PrivatePath,
        publicLinkedType: Type,
        privateLinkedType: Type,
        collectionName: String,
        collectionDescription: String,
        collectionSquareImage: String,
        collectionBannerImage: String,
        collectionExternalURL: String,
        royalties: [MetadataViews.Royalty]
    ) {
        self.id = id
        self.name = name
        self.description = description
        self.thumbnail = thumbnail
        self.externalURL = externalURL
        self.storagePath = storagePath
        self.publicPath = publicPath
        self.privatePath = privatePath
        self.publicLinkedType = publicLinkedType
        self.privateLinkedType = privateLinkedType
        self.collectionName = collectionName
        self.collectionDescription = collectionDescription
        self.collectionSquareImage = collectionSquareImage
        self.collectionBannerImage = collectionBannerImage
        self.collectionExternalURL = collectionExternalURL
        self.royalties = royalties
    }
}

pub fun main(ownerAddress: Address): {String: [NFT]} {
    let account = getAuthAccount(ownerAddress)
    let items: [MetadataViews.NFTView] = []
    let data: {String: [NFT]} = {}

    NFTCatalog.forEachCatalogKey(fun (collectionIdentifier: String):Bool {
        let value = NFTCatalog.getCatalogEntry(collectionIdentifier: collectionIdentifier)!
        let keyHash = String.encodeHex(HashAlgorithm.SHA3_256.hash(collectionIdentifier.utf8))
        let tempPathStr = "catalog".concat(keyHash)
        let tempPublicPath = PublicPath(identifier: tempPathStr)!

        account.link<&{MetadataViews.ResolverCollection}>(
            tempPublicPath,
            target: value.collectionData.storagePath
        )

        let collectionCap = account.getCapability<&AnyResource{MetadataViews.ResolverCollection}>(tempPublicPath)

        if !collectionCap.check() {
            return true
        }

        let views = NFTRetrieval.getNFTViewsFromCap(collectionIdentifier: collectionIdentifier, collectionCap: collectionCap)
        let items: [NFT] = []

        for view in views {
            let displayView = view.display
            let externalURLView = view.externalURL
            let collectionDataView = view.collectionData
            let collectionDisplayView = view.collectionDisplay
            let royaltyView = view.royalties

            if (displayView == nil || externalURLView == nil || collectionDataView == nil || collectionDisplayView == nil || royaltyView == nil) {
                // Bad NFT. Skipping....
                return true
            }

            items.append(
                NFT(
                    id: view.id,
                    name: displayView!.name,
                    description: displayView!.description,
                    thumbnail: displayView!.thumbnail.uri(),
                    externalURL: externalURLView!.url,
                    storagePath: collectionDataView!.storagePath,
                    publicPath: collectionDataView!.publicPath,
                    privatePath: collectionDataView!.providerPath,
                    publicLinkedType: collectionDataView!.publicLinkedType,
                    privateLinkedType: collectionDataView!.providerLinkedType,
                    collectionName: collectionDisplayView!.name,
                    collectionDescription: collectionDisplayView!.description,
                    collectionSquareImage: collectionDisplayView!.squareImage.file.uri(),
                    collectionBannerImage: collectionDisplayView!.bannerImage.file.uri(),
                    collectionExternalURL: collectionDisplayView!.externalURL.url,
                    royalties: royaltyView!.getRoyalties()
                )
            )
        }

        data[collectionIdentifier] = items
        return true
    })

    return data
}


 
  `,
      //@ts-ignore
      args: (arg, t) => [arg(ad.Address, t.Address)],
    });
    console.log(response);
  }

  async getNftsByAddress(Address: string) {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    const ad = Address;

    console.log("this is the add", ad);
    const response = await fcl.query({
      cadence: `
    import ExampleNFT from ${process.env.contractAddress}
    import MetadataViews from 0x631e88ae7f1d7c20
   pub fun main(address: Address): [{String: AnyStruct}] {
    let account = getAccount(address)
    let collection = account
        .getCapability(ExampleNFT.CollectionPublicPath)
        .borrow<&{ExampleNFT.ExampleNFTCollectionPublic}>()
        ?? panic("Could not borrow a reference to the collection")
    let nft = collection.getIDs()
       var nfts: [{String: AnyStruct}] = []
    for id in nft {
       
       let nft = collection.borrowExampleNFT(id: id)!
    // Get the basic display information for this NFT
     let display = MetadataViews.getDisplay(nft)!
    	
        
      let nftData = {"id": UInt64(id), "metadata": {"name": display.name, "description": display.description, "thumbnail": display.thumbnail.uri()}}
        nfts.append(nftData)
    }
  
    
    return nfts
}
 
  `,
      //@ts-ignore
      args: (arg, t) => [arg(ad.Address, t.Address)],
    });
    console.log(response);
  }

  async getAccountCollection(address: string) {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    const ad = address;

    console.log("this is the add", ad);
    const response = await fcl.query({
      cadence: `
      import MetadataViews from 0x631e88ae7f1d7c20
import NFTCatalog from 0x324c34e1c517e4db

import NFTRetrieval from 0x324c34e1c517e4db

pub fun main(ownerAddress: Address): {String: Number} {
    let account = getAuthAccount(ownerAddress)
    let items: {String: Number} = {}

    NFTCatalog.forEachCatalogKey(fun (collectionIdentifier: String):Bool {
        let value = NFTCatalog.getCatalogEntry(collectionIdentifier: collectionIdentifier)!
        let keyHash = String.encodeHex(HashAlgorithm.SHA3_256.hash(collectionIdentifier.utf8))
        let tempPathStr = "catalog".concat(keyHash)
        let tempPublicPath = PublicPath(identifier: tempPathStr)!

        account.link<&{MetadataViews.ResolverCollection}>(
            tempPublicPath,
            target: value.collectionData.storagePath
        )

        let collectionCap = account.getCapability<&AnyResource{MetadataViews.ResolverCollection}>(tempPublicPath)

        if !collectionCap.check() {
            return true
        }

        let count = NFTRetrieval.getNFTCountFromCap(collectionIdentifier: collectionIdentifier, collectionCap: collectionCap)

        if count != 0 {
            items[collectionIdentifier] = count
        }
        return true
    })

    return items
}
   `,
      //@ts-ignore
      args: (arg, t) => [arg(ad.Address, t.Address)],
    });

    console.log(response);
  }

  async getNftById(adress : String ,id: number ) {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    const ad = id;

    console.log("this is the add", ad);


    const response = await fcl.query({
      cadence: `
         import ExampleNFT from ${process.env.contractAddress}
import MetadataViews from 0x631e88ae7f1d7c20
/// This script gets all the view-based metadata associated with the specified NFT
/// and returns it as a single struct
pub struct NFT {
    pub let name: String
    pub let description: String
    pub let thumbnail: String
    pub let owner: Address
    pub let type: String
    pub let royalties: [MetadataViews.Royalty]
    pub let externalURL: String
    pub let serialNumber: UInt64
    pub let collectionPublicPath: PublicPath
    pub let collectionStoragePath: StoragePath
    pub let collectionProviderPath: PrivatePath
    pub let collectionPublic: String
    pub let collectionPublicLinkedType: String
    pub let collectionProviderLinkedType: String
    pub let collectionName: String
    pub let collectionDescription: String
    pub let collectionExternalURL: String
    pub let collectionSquareImage: String
    pub let collectionBannerImage: String
    pub let collectionSocials: {String: String}
    pub let edition: MetadataViews.Edition
    pub let traits: MetadataViews.Traits
pub let medias: MetadataViews.Medias?
pub let license: MetadataViews.License?
    init(
        name: String,
        description: String,
        thumbnail: String,
        owner: Address,
        nftType: String,
        royalties: [MetadataViews.Royalty],
        externalURL: String,
        serialNumber: UInt64,
        collectionPublicPath: PublicPath,
        collectionStoragePath: StoragePath,
        collectionProviderPath: PrivatePath,
        collectionPublic: String,
        collectionPublicLinkedType: String,
        collectionProviderLinkedType: String,
        collectionName: String,
        collectionDescription: String,
        collectionExternalURL: String,
        collectionSquareImage: String,
        collectionBannerImage: String,
        collectionSocials: {String: String},
        edition: MetadataViews.Edition,
        traits: MetadataViews.Traits,
medias:MetadataViews.Medias?,
license:MetadataViews.License?
    ) {
        self.name = name
        self.description = description
        self.thumbnail = thumbnail
        self.owner = owner
        self.type = nftType
        self.royalties = royalties
        self.externalURL = externalURL
        self.serialNumber = serialNumber
        self.collectionPublicPath = collectionPublicPath
        self.collectionStoragePath = collectionStoragePath
        self.collectionProviderPath = collectionProviderPath
        self.collectionPublic = collectionPublic
        self.collectionPublicLinkedType = collectionPublicLinkedType
        self.collectionProviderLinkedType = collectionProviderLinkedType
        self.collectionName = collectionName
        self.collectionDescription = collectionDescription
        self.collectionExternalURL = collectionExternalURL
        self.collectionSquareImage = collectionSquareImage
        self.collectionBannerImage = collectionBannerImage
        self.collectionSocials = collectionSocials
        self.edition = edition
        self.traits = traits
self.medias=medias
self.license=license
    }
}
pub fun main(address: Address, id: UInt64): NFT {
    let account = getAccount(address)
    let collection = account
        .getCapability(ExampleNFT.CollectionPublicPath)
        .borrow<&{ExampleNFT.ExampleNFTCollectionPublic}>()
        ?? panic("Could not borrow a reference to the collection")
    let nft = collection.borrowExampleNFT(id: id)!
    // Get the basic display information for this NFT
    let display = MetadataViews.getDisplay(nft)!
    // Get the royalty information for the given NFT
    let royaltyView = MetadataViews.getRoyalties(nft)!
    let externalURL = MetadataViews.getExternalURL(nft)!
    let collectionDisplay = MetadataViews.getNFTCollectionDisplay(nft)!
    let nftCollectionView = MetadataViews.getNFTCollectionData(nft)!
    let nftEditionView = MetadataViews.getEditions(nft)!
    let serialNumberView = MetadataViews.getSerial(nft)!
    
    let owner: Address = nft.owner!.address!
    let nftType = nft.getType()
    let collectionSocials: {String: String} = {}
    for key in collectionDisplay.socials.keys {
        collectionSocials[key] = collectionDisplay.socials[key]!.url
    }
let traits = MetadataViews.getTraits(nft)!
let medias=MetadataViews.getMedias(nft)
let license=MetadataViews.getLicense(nft)
    return NFT(
        name: display.name,
        description: display.description,
        thumbnail: display.thumbnail.uri(),
        owner: owner,
        nftType: nftType.identifier,
        royalties: royaltyView.getRoyalties(),
        externalURL: externalURL.url,
        serialNumber: serialNumberView.number,
        collectionPublicPath: nftCollectionView.publicPath,
        collectionStoragePath: nftCollectionView.storagePath,
        collectionProviderPath: nftCollectionView.providerPath,
        collectionPublic: nftCollectionView.publicCollection.identifier,
        collectionPublicLinkedType: nftCollectionView.publicLinkedType.identifier,
        collectionProviderLinkedType: nftCollectionView.providerLinkedType.identifier,
        collectionName: collectionDisplay.name,
        collectionDescription: collectionDisplay.description,
        collectionExternalURL: collectionDisplay.externalURL.url,
        collectionSquareImage: collectionDisplay.squareImage.file.uri(),
        collectionBannerImage: collectionDisplay.bannerImage.file.uri(),
        collectionSocials: collectionSocials,
        edition: nftEditionView.infoList[0],
        traits: traits,
medias:medias,
license:license
    )
}
     `,
        //@ts-ignore
      args: (arg, t) => [
        arg(adress, t.Address),
        arg(ad, t.UInt64)
      ],

    });
    console.log(response);

  }


}

export default new FlowService();