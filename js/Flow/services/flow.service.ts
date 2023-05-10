import * as dotenv from "dotenv";
import * as url from "url";

const fcl = require("@onflow/fcl");

dotenv.config();
class FlowService {

  async getAccountDetails() {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
     try{
       //  const account = await fcl.send([fcl.getAccount(process.env.contractAddress)]).then(fcl.decode);

         //    return account;


     }catch (error) {
         return error;
     }
  }


  async getAllNFTs(Address: string , chain: string) {
    const ad = Address;

      if (chain === "mainnet") {
          var MetadataViews = process.env.MetadataViews_mainnet;
          var NFTCatalog = process.env.NFTCatalog_mainnet;
          var NFTRetrieval = process.env.NFTRetrieval_mainnet;
          var url = "https://access-mainnet-beta.onflow.org";
      }
      else
      {
          var MetadataViews =process.env.MetadataViews_testnet;
          var NFTCatalog =process.env.NFTCatalog_testnet;
          var NFTRetrieval = process.env.NFTRetrieval_testnet;
          var url = "https://access-testnet.onflow.org";
      }

      fcl.config().put("accessNode.api", url);

      console.log(ad);
    try {

        const response = await fcl.query({
            cadence: `
import MetadataViews from ${MetadataViews}
import NFTCatalog from ${NFTCatalog}
import NFTRetrieval from ${NFTRetrieval}

pub struct NFT {
    pub let id: UInt64
    pub let name: String
    pub let description: String
    pub let thumbnail: String
    pub let externalURL: String
    pub let collectionStoragePath: StoragePath
    pub let collectionPublicPath: PublicPath
    pub let collectionPrivatePath: PrivatePath
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
        collectionStoragePath: StoragePath,
        collectionPublicPath: PublicPath,
        collectionPrivatePath: PrivatePath,
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
        self.collectionStoragePath = collectionStoragePath
        self.collectionPublicPath = collectionPublicPath
        self.collectionPrivatePath = collectionPrivatePath
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
                    collectionStoragePath: collectionDataView!.storagePath,
                    collectionPublicPath: collectionDataView!.publicPath,
                    collectionPrivatePath: collectionDataView!.providerPath,
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
            args: (arg, t) => [arg(Address, t.Address)],
        });
        return response;
    }
    catch (error) {
        return error;
    }
  }

  async getNftsByAddressInCollection(Address: string ,collectionPath : string ,chain: string) {

      if (chain === "mainnet") {
          var MetadataViews = process.env.MetadataViews_mainnet;
          var url = "https://access-mainnet-beta.onflow.org";
      }
      else
      {
          var MetadataViews =process.env.MetadataViews_testnet;
          var url = "https://access-testnet.onflow.org";
      }

      fcl.config().put("accessNode.api", url);



    try {
        const response = await fcl.query({
            cadence: `

import MetadataViews from ${MetadataViews}

/// This script gets all the view-based metadata associated with the specified NFT
/// and returns it as a single struct

pub fun main(address: Address): [{String: AnyStruct}] {
    let account = getAccount(address)

    let collection = account
        .getCapability(${collectionPath})
        .borrow<&{MetadataViews.ResolverCollection}>()
        ?? panic("Could not borrow a reference to the collection")

    let nft = collection.getIDs()
       var nfts: [{String: AnyStruct}] = []
    for id in nft {
       
       let nft = collection.borrowViewResolver(id: id)

    // Get the basic display information for this NFT
         let display = MetadataViews.getDisplay(nft)!

        let identifier = nft.getType().identifier
        let traits = MetadataViews.getTraits(nft)!
        
        let externalURL = MetadataViews.getExternalURL(nft)!.url

        
        

        
      let nftData = {
        "id": UInt64(id), 
        
         "name": display.name,
         "description": display.description, 
         "thumbnail": display.thumbnail.uri(),
         "identifier": identifier,
         "traits": traits,
         "externalURL": externalURL
            
       
          
      }
        nfts.append(nftData)
    }
  
    

    return nfts
}
 
  `,
            //@ts-ignore
            args: (arg, t) => [arg(Address, t.Address)],
        });
        return response;
    }catch (error) {
        return error;
    }
  }

  async getAccountCollection(address: string) {
    fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    const ad = address;
    try {
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

        return response;
    }catch (error) {
        return error;
    }
  }

  async getNftById(account_id : String  , contractAddress: string,  collectionPublicPath : string,id: number, chain: string) {
      if (chain === "mainnet") {
          var MetadataViews = process.env.MetadataViews_mainnet;
          var url = "https://access-mainnet-beta.onflow.org";
      }
      else
      {
          var MetadataViews =process.env.MetadataViews_testnet;
          var url = "https://access-testnet.onflow.org";
      }

      fcl.config().put("accessNode.api", url);

    const ad = id;

    try {
        const response = await fcl.query({
            cadence: `
        
import MetadataViews from ${MetadataViews}

// pub struct NFT {
//     pub let name: String
//     pub let description: String
//     pub let thumbnail: String
//     pub let owner: Address
//     pub let type: String
//     pub let royalties: [MetadataViews.Royalty]
//     pub let externalURL: String
//     pub let serialNumber: UInt64
//     pub let collectionPublicPath: PublicPath
//     pub let collectionStoragePath: StoragePath
//     pub let collectionProviderPath: PrivatePath
//     pub let collectionPublic: String
//     pub let collectionPublicLinkedType: String
//     pub let collectionProviderLinkedType: String
//     pub let collectionName: String
//     pub let collectionDescription: String
//     pub let collectionExternalURL: String
//     pub let collectionSquareImage: String
//     pub let collectionBannerImage: String
//     pub let collectionSocials: {String: String}
//    pub let edition: MetadataViews.Edition
//     pub let traits: MetadataViews.Traits
//    pub let medias: MetadataViews.Medias?
//    pub let license: MetadataViews.License?
//     init(
//         name: String,
//         description: String,
//         thumbnail: String,
//         owner: Address,
//         nftType: String,
//         royalties: [MetadataViews.Royalty],
//         externalURL: String,
//         serialNumber: UInt64,
//         collectionPublicPath: PublicPath,
//         collectionStoragePath: StoragePath,
//         collectionProviderPath: PrivatePath,
//         collectionPublic: String,
//         collectionPublicLinkedType: String,
//         collectionProviderLinkedType: String,
//         collectionName: String,
//         collectionDescription: String,
//         collectionExternalURL: String,
//         collectionSquareImage: String,
//         collectionBannerImage: String,
//         collectionSocials: {String: String},
//         edition: MetadataViews.Edition,
//        traits: MetadataViews.Traits,
//        medias:MetadataViews.Medias?,
//        license:MetadataViews.License?
//     ) {
//         self.name = name
//         self.description = description
//         self.thumbnail = thumbnail
//         self.owner = owner
//         self.type = nftType
//         self.royalties = royalties
//         self.externalURL = externalURL
//          self.serialNumber = serialNumber
//         self.collectionPublicPath = collectionPublicPath
//         self.collectionStoragePath = collectionStoragePath
//         self.collectionProviderPath = collectionProviderPath
//         self.collectionPublic = collectionPublic
//         self.collectionPublicLinkedType = collectionPublicLinkedType
//         self.collectionProviderLinkedType = collectionProviderLinkedType
//         self.collectionName = collectionName
//         self.collectionDescription = collectionDescription
//         self.collectionExternalURL = collectionExternalURL
//         self.collectionSquareImage = collectionSquareImage
//         self.collectionBannerImage = collectionBannerImage
//         self.collectionSocials = collectionSocials
//         self.edition = edition
//        self.traits = traits
//        self.medias=medias
//        self.license=license
//     }
// }
// pub fun main(address: Address, id: UInt64): NFT {
//     let account = getAccount(address)
//     let collection = account
//         .getCapability(/public/MomentCollection)
//         .borrow<&{.MomentCollectionPublic}>()
//         ?? panic("Could not borrow a reference to the collection")
//     let nft = collection.borrowNFT(id: id)!
//     // Get the basic display information for this NFT
//     let display = MetadataViews.getDisplay(nft)!
//     // Get the royalty information for the given NFT
//     let royaltyView = MetadataViews.getRoyalties(nft)!
//     let externalURL = MetadataViews.getExternalURL(nft)!
//     let collectionDisplay = MetadataViews.getNFTCollectionDisplay(nft)!
//     let nftCollectionView = MetadataViews.getNFTCollectionData(nft)!
//     let nftEditionView = MetadataViews.getEditions(nft)!
//     let serialNumberView = MetadataViews.getSerial(nft)!
//    
//     let owner: Address = nft.owner!.address!
//     let nftType = nft.getType()
//     let collectionSocials: {String: String} = {}
//     for key in collectionDisplay.socials.keys {
//         collectionSocials[key] = collectionDisplay.socials[key]!.url
//     }
// let traits = MetadataViews.getTraits(nft)!
// let medias=MetadataViews.getMedias(nft)
// let license=MetadataViews.getLicense(nft)
//     return NFT(
//         name: display.name,
//         description: display.description,
//         thumbnail: display.thumbnail.uri(),
//         owner: owner,
//         nftType: nftType.identifier,
//         royalties: royaltyView.getRoyalties(),
//         externalURL: externalURL.url,
//         serialNumber: serialNumberView.number,
//         collectionPublicPath: nftCollectionView.publicPath,
//         collectionStoragePath: nftCollectionView.storagePath,
//         collectionProviderPath: nftCollectionView.providerPath,
//         collectionPublic: nftCollectionView.publicCollection.identifier,
//         collectionPublicLinkedType: nftCollectionView.publicLinkedType.identifier,
//         collectionProviderLinkedType: nftCollectionView.providerLinkedType.identifier,
//         collectionName: collectionDisplay.name,
//         collectionDescription: collectionDisplay.description,
//         collectionExternalURL: collectionDisplay.externalURL.url,
//         collectionSquareImage: collectionDisplay.squareImage.file.uri(),
//         collectionBannerImage: collectionDisplay.bannerImage.file.uri(),
//         collectionSocials: collectionSocials,
//         edition: nftEditionView.infoList[0],
//        traits: traits,
//         medias:medias,
//         license:license
//     )
// }



  pub struct NFTView {
          pub let id: UInt64
          pub let name: String
          pub let description: String
          pub let externalURL: String
          pub let thumbnail : String
        
          init(
              id: UInt64,
              name: String,
              description: String,
              externalURL: String,
              thumbnail : String
              
          ) {
              self.id = id
              self.name = name
              self.description = description
              self.externalURL = externalURL
              self.thumbnail = thumbnail
          }
        }
/// This script gets all the view-based metadata associated with the specified NFT
/// and returns it as a single struct

pub fun main(address: Address, id: UInt64) : NFTView {
    let account = getAccount(address)

    
    
 let collection = account
              .getCapability(${collectionPublicPath})
              .borrow<&{MetadataViews.ResolverCollection}>()
              ?? panic("Could not borrow a reference to the collection")
        
          let viewResolver = collection.borrowViewResolver(id: id)!
        
          let nftView = MetadataViews.getNFTView(id: id, viewResolver : viewResolver)
        
          let collectionSocials: {String: String} = {}
          for key in nftView.collectionDisplay!.socials.keys {
              collectionSocials[key] = nftView.collectionDisplay!.socials[key]!.url
          }

            

 return NFTView(
              id: nftView.id,
              name: nftView.display!.name,
              description: nftView.display!.description,
              externalURL: nftView.externalURL!.url,
              thumbnail :  nftView.display!.thumbnail.uri()
          )
   
}
 
     `,
            //@ts-ignore
            args: (arg, t) => [
                arg(account_id, t.Address),
                arg(ad, t.UInt64)
            ],

        });
        return response;
    }catch (error) {
        return error;
    }

  }

  async getContractName() {
     fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
    try {
        const response = await fcl.send([
            fcl.script`
//         import NFTCatalog from 0x324c34e1c517e4db
//
// pub fun main(): {String : Bool}? {
//     let nftTypeIdentifier = "A.1c5033ad60821c97.Wearables.NFT"
//     return NFTCatalog.getCollectionsForType(nftTypeIdentifier: CompositeType(nftTypeIdentifier)!.identifier)
// }

import MetadataViews from 0x631e88ae7f1d7c20
import NFTRetrieval from 0x324c34e1c517e4db

pub struct DisplayView {
    pub let name : String
    pub let description : String
    pub let thumbnail : String

    init (
        name : String,
        description : String,
        thumbnail : String
    ) {
        self.name = name
        self.description = description
        self.thumbnail = thumbnail
    }
}

pub struct ExternalURLView {
    pub let externalURL : String

    init (
        externalURL : String
    ) {
        self.externalURL = externalURL
    }
}

pub struct NFTCollectionDataView {
    pub let storagePath : StoragePath
    pub let publicPath : PublicPath
    pub let privatePath: PrivatePath
    pub let publicLinkedType: Type
    pub let privateLinkedType: Type

    init (
        storagePath : StoragePath,
        publicPath : PublicPath,
        privatePath : PrivatePath,
        publicLinkedType : Type,
        privateLinkedType : Type,
    ) {
        self.storagePath = storagePath
        self.publicPath = publicPath
        self.privatePath = privatePath
        self.publicLinkedType = publicLinkedType
        self.privateLinkedType = privateLinkedType
    }
}

pub struct NFTCollectionDisplayView {
    pub let collectionName : String
    pub let collectionDescription: String
    pub let collectionSquareImage : MetadataViews.Media
    pub let collectionBannerImage : MetadataViews.Media
    pub let externalURL : String
    pub let socials : {String: MetadataViews.ExternalURL}

    init (
        collectionName : String,
        collectionDescription : String,
        collectionSquareImage : MetadataViews.Media,
        collectionBannerImage : MetadataViews.Media,
        externalURL : String,
        socials : {String: MetadataViews.ExternalURL}
    ) {
        self.collectionName = collectionName
        self.collectionDescription = collectionDescription
        self.collectionSquareImage = collectionSquareImage
        self.collectionBannerImage = collectionBannerImage
        self.externalURL = externalURL
        self.socials = socials
    }
}

pub struct RoyaltiesView {
    pub let royalties: [MetadataViews.Royalty]

    init (
        royalties : [MetadataViews.Royalty]
    ) {
        self.royalties = royalties
    }
}

pub struct NFT {
    pub let id : UInt64
    pub let display : DisplayView?
    pub let externalURL : ExternalURLView?
    pub let nftCollectionData : NFTCollectionDataView?
    pub let nftCollectionDisplay : NFTCollectionDisplayView?
    pub let royalties : RoyaltiesView?

    init(
            id: UInt64,
            display : DisplayView?,
            externalURL : ExternalURLView?,
            nftCollectionData : NFTCollectionDataView?,
            nftCollectionDisplay : NFTCollectionDisplayView?,
            royalties : RoyaltiesView?
    ) {
        self.id = id
        self.display = display
        self.externalURL = externalURL
        self.nftCollectionData = nftCollectionData
        self.nftCollectionDisplay = nftCollectionDisplay
        self.royalties = royalties
}

pub fun getMapping() : {String : AnyStruct} {
    return {
        "Id" : self.id,
        "Display" : self.display,
        "ExternalURL" : self.externalURL,
        "NFTCollectionData" : self.nftCollectionData,
        "NFTCollectionDisplay" : self.nftCollectionDisplay,
        "Royalties" : self.royalties
    }
}

}

pub fun main(): [{String : AnyStruct}]    {
    
    let storagePathIdentifier = "exampleNFTCollection"

    let owner = getAuthAccount(${process.env.contractAddress})
    
    let tempPathStr = "getNFTsInAccountFromPathNFTCatalog"
    let tempPublicPath = PublicPath(identifier: tempPathStr)!
    owner.link<&{MetadataViews.ResolverCollection}>(
        tempPublicPath,
        target: StoragePath(identifier : storagePathIdentifier)!
    )

    let collectionCap = owner.getCapability<&AnyResource{MetadataViews.ResolverCollection}>(tempPublicPath)
    assert(collectionCap.check(), message: "MetadataViews Collection is not set up properly, ensure the Capability was created/linked correctly.")
    let collection = collectionCap.borrow()!
    assert(collection.getIDs().length > 0, message: "No NFTs exist in this collection, ensure the provided account has at least 1 NFTs.")

    let data : [{String : AnyStruct}] = []

    for nftID in collection.getIDs() {
        data.append(getNFTData(nftID: nftID, collection: collection))
    }

    return data
}


pub fun getNFTData(nftID: UInt64, collection: &AnyResource{MetadataViews.ResolverCollection} ): {String : AnyStruct} {
    let nftResolver = collection.borrowViewResolver(id: nftID)
    let nftViews = MetadataViews.getNFTView(
        id : nftID,
        viewResolver: nftResolver
    )

    let displayView = nftViews.display
    let externalURLView = nftViews.externalURL
    let collectionDataView = nftViews.collectionData
    let collectionDisplayView = nftViews.collectionDisplay
    let royaltyView = nftViews.royalties

    var display : DisplayView? = nil
    if displayView != nil {
        display = DisplayView(
            name : displayView!.name,
            description : displayView!.description,
            thumbnail : displayView!.thumbnail.uri()
        )
    }

    var externalURL : ExternalURLView? = nil
    if externalURLView != nil {
        externalURL = ExternalURLView(
            externalURL : externalURLView!.url,
        )
    }

    var nftCollectionData : NFTCollectionDataView? = nil
    if collectionDataView != nil {
        nftCollectionData = NFTCollectionDataView(
            storagePath : collectionDataView!.storagePath,
            publicPath : collectionDataView!.publicPath,
            privatePath : collectionDataView!.providerPath,
            publicLinkedType : collectionDataView!.publicLinkedType,
            privateLinkedType : collectionDataView!.providerLinkedType,
        )
    }

    var nftCollectionDisplay : NFTCollectionDisplayView? = nil
    if collectionDisplayView != nil {
        nftCollectionDisplay = NFTCollectionDisplayView(
            collectionName : collectionDisplayView!.name,
            collectionDescription : collectionDisplayView!.description,
            collectionSquareImage : collectionDisplayView!.squareImage,
            collectionBannerImage : collectionDisplayView!.bannerImage,
            externalURL : collectionDisplayView!.externalURL.url,
            socials : collectionDisplayView!.socials
        )
    }

    var royalties : RoyaltiesView? = nil
    if royaltyView != nil {
        royalties = RoyaltiesView(
            royalties : royaltyView!.getRoyalties()
        )
    }

    return NFT(
        id: nftID,
        display : display,
        externalURL : externalURL,
        nftCollectionData : nftCollectionData,
        nftCollectionDisplay : nftCollectionDisplay,
        royalties : royalties
    ).getMapping()
}

`,

        ]);
        const data = await fcl.decode(response);

        console.log(data[0].NFTCollectionData);
        return data[0].NFTCollectionData;
    }catch (error) {
        console.log(error);
    }

  }


  // get nfts from account + collection
  async getID_script(){
      fcl.config().put("accessNode.api", "https://access-testnet.onflow.org");
      const adress = process.env.contractAddress;
      const response = await fcl.query({
            cadence: `
//             import MetadataViews from 0x631e88ae7f1d7c20;
// pub fun main(): AnyStruct? {
//    
//   let account = getAccount(0xa9ccb9756a0ee7eb)
//   let collection = account
//     .getCapability(/public/exampleNFTCollection)
//        .borrow<&{MetadataViews.ResolverCollection}>()
//     ?? panic("Could not borrow a reference to the collection")
//   let IDs = collection
//    
//   return IDs;
// }
import MetadataViews from 0x631e88ae7f1d7c20;
pub fun main(): NFTResult {
  let id : UInt64 = 0
  let account = getAccount(0xa9ccb9756a0ee7eb)
  let collection = account
      .getCapability(/public/exampleNFTCollection) // Update the path here!
      .borrow<&{MetadataViews.ResolverCollection}>()
      ?? panic("Could not borrow a reference to the collection")
  let nft = collection.borrowViewResolver(id: id)
  var data = NFTResult()
  // Get the basic display information for this NFT
  if let view = nft.resolveView(Type<MetadataViews.Display>()) {
    let display = view as! MetadataViews.Display
    data.name = display.name
    data.description = display.description
    data.thumbnail = display.thumbnail.uri()
  }
  // The owner is stored directly on the NFT object
  let owner: Address = nft.owner!.address
  data.owner = owner
  return data
}
pub struct NFTResult {
  pub(set) var name: String
  pub(set) var description: String
  pub(set) var thumbnail: String
  pub(set) var owner: Address
  pub(set) var type: String
  init() {
    self.name = ""
    self.description = ""
    self.thumbnail = ""
    self.owner = 0x0
    self.type = ""
  }
}

            `,

        });

        console.log(response);
      }


}

export default new FlowService();