import { ContractDeployment } from "./../dto/deploy.contract.dto";
import * as nearAPI from "near-api-js";
import * as dotenv from "dotenv";
const { keyStores, KeyPair, Contract, connect } = nearAPI;
import BN from "bn.js";
import fs from "fs";
import { CreateSubAccount } from "../dto/create.subaccount.dto";

import { DeployContractWithCustomInit } from "../dto/deploy.contract.customInit";
import { NftMint } from "../dto/nft.mint.dto";
import {providers} from "near-api-js";
dotenv.config();
// near servie
class NearService {
  myKeyStore = new keyStores.InMemoryKeyStore();
  PRIVATE_KEY = process.env.NEAR_PRIVATE_KEY || "5rzEcWjD3dD7472Wp4pM7PXLM4rLiA8KYbtsQ2LZzEr4uurPLZZRQum77mkmLLcjZU7YEK7R9DKoY7ErpYyvX2wr";

  keyPair = KeyPair.fromString(this.PRIVATE_KEY);
  // adds the keyPair you created to keyStore
  async addKeyPairToKeyStore(accountid: string, networkid: string) {
    await this.myKeyStore.setKey(networkid, accountid, this.keyPair);
  }

  connectionConfig = {
    networkId: "testnet",
    keyStore: this.myKeyStore, // first create a key store
    nodeUrl: "https://rpc.testnet.near.org",
    walletUrl: "https://wallet.testnet.near.org",
    helperUrl: "https://helper.testnet.near.org",
  };

  async getAccountDetails() {
    const near = await nearAPI.connect(this.connectionConfig);
    const account = await near.account("khaled_lightency1.testnet");
    return account.getAccountBalance();
  }

  async createAccount(createSubAccount: CreateSubAccount) {
    try {
      let rpcUrl;
      let networkId;
      if (createSubAccount.chain === "testnet") {
        rpcUrl = "https://rpc.testnet.near.org";
        networkId = "testnet";
      } else if (createSubAccount.chain === "mainnet") {
        rpcUrl = "https://rpc.mainnet.near.org";
        networkId = "mainnet";
      } else {
        throw new Error("Chain parameter is not defined");
      }
      this.addKeyPairToKeyStore(
        createSubAccount.account_id,
        createSubAccount.chain
      );
      this.connectionConfig = {
        networkId: networkId,
        keyStore: this.myKeyStore, // first create a key store
        nodeUrl: rpcUrl,
        walletUrl: "",
        helperUrl: "",
      };
      const near = await nearAPI.connect(this.connectionConfig);
      const account = await near.account(createSubAccount.account_id);
      const publickey = this.keyPair.getPublicKey().toString();
      const PK = publickey.replace("ed25519:", "");

      const amount = new BN(createSubAccount.amount);
     const response = account.createAccount(
        createSubAccount.newAccountId,
        PK,
        amount.mul(new BN("1000000000000000000000000"))
      );

      // return (await response).transaction.hash;

      
      return JSON.stringify( (await response).transaction.hash );
   
    } catch (err) {
      console.log(err);
    }
  }
  // deploy contract with default metadata
  async deployContract(contractDeployment: ContractDeployment) {
    this.addKeyPairToKeyStore(
      contractDeployment.account_id,
      contractDeployment.chain
    );
    const near = await nearAPI.connect(this.connectionConfig);
    const account = await near.account(contractDeployment.account_id);

    const response = await account.deployContract(
      fs.readFileSync("near/smart contract/waltid_nftkit.wasm")
    );
    console.log(response);

    const contract = new Contract(account, contractDeployment.account_id, {
      viewMethods: [],
      changeMethods: ["new_default_meta"],
    });

    const GAS = new BN("100000000000000");
    // @ts-ignore
    contract.new_default_meta({
      args: {
        owner_id: contractDeployment.account_id,
      },
      gas: GAS,
    });
    return JSON.stringify((await response).transaction.hash);
  }

  // deploy contract with custom metadata
  async deployContractWithCustomMetadata(
    ContractDeploymentWithCustomMetadata: DeployContractWithCustomInit
  ) {
    let rpcUrl;
    let networkId;
    if (ContractDeploymentWithCustomMetadata.chain === "testnet") {
      rpcUrl = "https://rpc.testnet.near.org";
      networkId = "testnet";
    } else if (ContractDeploymentWithCustomMetadata.chain === "mainnet") {
      rpcUrl = "https://rpc.mainnet.near.org";
      networkId = "mainnet";
    } else {
      throw new Error("Chain parameter is not defined");
    }
    this.addKeyPairToKeyStore(
      ContractDeploymentWithCustomMetadata.account_id,
      ContractDeploymentWithCustomMetadata.chain
    );
    this.connectionConfig = {
      networkId: networkId,
      keyStore: this.myKeyStore, // first create a key store
      nodeUrl: rpcUrl,
      walletUrl: "",
      helperUrl: "",
    };
  
    const near = await nearAPI.connect(this.connectionConfig);
    const account = await near.account(
      ContractDeploymentWithCustomMetadata.account_id
    );

    const response = await account.deployContract(
      fs.readFileSync("near/smart contract/waltid_nftkit.wasm")
    );
    console.log(response);

    const contract = new Contract(
      account,
      ContractDeploymentWithCustomMetadata.account_id,
      {
        viewMethods: [],
        changeMethods: ["new"],
      }
    );

    const GAS = new BN("100000000000000");
    // @ts-ignore
    contract.new({
      args: {
        owner_id: ContractDeploymentWithCustomMetadata.account_id,
        metadata: {
          spec: ContractDeploymentWithCustomMetadata.spec,
          name: ContractDeploymentWithCustomMetadata.name,
          symbol: ContractDeploymentWithCustomMetadata.symbol,
          icon: ContractDeploymentWithCustomMetadata.icon,
          base_uri: ContractDeploymentWithCustomMetadata.base_uri,
          reference: ContractDeploymentWithCustomMetadata.reference,
          reference_hash: ContractDeploymentWithCustomMetadata.reference_hash,
        },
      },
      gas: GAS,
    });
    return JSON.stringify((await response).transaction.hash);
  }

  async mintToken(nftmint: NftMint) {

    let rpcUrl;
    let networkId;
    if (nftmint.chain === "testnet") {
      rpcUrl = "https://rpc.testnet.near.org";
      networkId = "testnet";
    } else if (nftmint.chain === "mainnet") {
      rpcUrl = "https://rpc.mainnet.near.org";
      networkId = "mainnet";
    } else {
      throw new Error("Chain parameter is not defined");
    }
    this.addKeyPairToKeyStore(
      nftmint.account_id,
      nftmint.chain
    );
    this.connectionConfig = {
      networkId: networkId,
      keyStore: this.myKeyStore, // first create a key store
      nodeUrl: rpcUrl,
      walletUrl: "",
      helperUrl: "",
    };
    const near = await nearAPI.connect(this.connectionConfig);
    const account = await near.account(nftmint.account_id);

    const GAS = new BN("100000000000000");
    const Amount_deposited = new BN("100000000000000000000000");
    // @ts-ignore
    // let output =  contract.nft_mint({
    //   args: {
    //     token_id: nftmint.token_id,
    //     metadata: {
    //       title: nftmint.title,
    //       description: nftmint.description,
    //       media: nftmint.media,
    //       media_hash: null,
    //     },
    //
    //     receiver_id: nftmint.receiver_id,
    //   },
    //   gas: GAS,
    //   amount: Amount_deposited,
    // });
    const functionCallResponse = await account.functionCall({
      contractId: nftmint.contract_id,
      methodName: "nft_mint",
      args: {

              token_id: nftmint.token_id,
              metadata: {
                title: nftmint.title,
                description: nftmint.description,
                media: nftmint.media,
                media_hash: null,
              },

              receiver_id: nftmint.receiver_id,
            },

      gas: GAS,
      attachedDeposit: Amount_deposited,
    });

    return JSON.stringify(functionCallResponse.transaction.hash);
  }
}

export default new NearService();
