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


}


export default new FlowService();