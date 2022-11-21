"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const taquito_1 = require("@taquito/taquito");
const taquito_2 = require("@taquito/taquito");
const signer_1 = require("@taquito/signer");
const multiple_nft_fa2_private_collection_1 = require("../smart contract/multiple-nft-fa2-private-collection");
const single_nft_fa2_private_collection_1 = require("../smart contract/single-nft-fa2-private-collection");
const dotenv = __importStar(require("dotenv"));
class TezosService {
    deployContract(contractDeployment) {
        return __awaiter(this, void 0, void 0, function* () {
            dotenv.config();
            try {
                let rpcUrl;
                if (contractDeployment.chain == "TEZOS") {
                    rpcUrl = process.env.MAINNET_RPC_URL;
                }
                else if (contractDeployment.chain == "GHOSTNET") {
                    rpcUrl = process.env.GHOSTNET_RPC_URL;
                }
                else {
                    throw new Error('Chain parameter is not defined');
                }
                const tezos = new taquito_1.TezosToolkit(rpcUrl);
                tezos.setSignerProvider(new signer_1.InMemorySigner(process.env.PRIVATE_KEY));
                let code;
                let getStorage;
                if (contractDeployment.type == "SINGLE") {
                    code = single_nft_fa2_private_collection_1.code;
                    getStorage = single_nft_fa2_private_collection_1.getStorage;
                }
                else if (contractDeployment.type == "MULTIPLE") {
                    code = multiple_nft_fa2_private_collection_1.code;
                    getStorage = multiple_nft_fa2_private_collection_1.getStorage;
                }
                else {
                    throw new Error('Type parameter is not defined');
                }
                const operation = yield tezos.wallet.originate({
                    code: code,
                    init: getStorage(contractDeployment.owner)
                }).send();
                console.log(`Waiting for confirmation of origination...`);
                const contract = yield operation.contract();
                console.log(`Origination completed for ${contract.address}.`);
                return contract.address;
            }
            catch (error) {
                console.log(`Error: ${JSON.stringify(error, null, 2)}`);
            }
        });
    }
    mintToken(mintTokenDto) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                dotenv.config();
                let rpcUrl;
                if (mintTokenDto.chain == "TEZOS") {
                    rpcUrl = process.env.MAINNET_RPC_URL;
                }
                else if (mintTokenDto.chain == "GHOSTNET") {
                    rpcUrl = process.env.GHOSTNET_RPC_URL;
                }
                else {
                    throw new Error('Chain parameter is not defined');
                }
                const tezos = yield new taquito_1.TezosToolkit(rpcUrl);
                tezos.setSignerProvider(new signer_1.InMemorySigner(process.env.PRIVATE_KEY));
                const contract = yield tezos.wallet.at(mintTokenDto.fa2ContractAddress);
                const token_info = new taquito_2.MichelsonMap();
                token_info.set('', this.toHexString(mintTokenDto.metadata));
                const royalties = [];
                let op;
                if (mintTokenDto.amount) {
                    console.log("multiple");
                    op = yield contract.methods.mint(mintTokenDto.tokenId, mintTokenDto.owner, mintTokenDto.amount, token_info, royalties).send();
                }
                else {
                    console.log("single");
                    op = yield contract.methods.mint(mintTokenDto.tokenId, mintTokenDto.owner, token_info, royalties).send();
                }
                yield op.confirmation();
                return op.opHash;
            }
            catch (error) {
                console.log(`Error: ${JSON.stringify(error, null, 2)}`);
            }
        });
    }
    addMinter(addMinterDto) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                dotenv.config();
                let rpcUrl;
                if (addMinterDto.chain == "TEZOS") {
                    rpcUrl = process.env.MAINNET_RPC_URL;
                }
                else if (addMinterDto.chain == "GHOSTNET") {
                    rpcUrl = process.env.GHOSTNET_RPC_URL;
                }
                else {
                    throw new Error('Chain parameter is not defined');
                }
                const tezos = yield new taquito_1.TezosToolkit(rpcUrl);
                tezos.setSignerProvider(new signer_1.InMemorySigner(process.env.PRIVATE_KEY));
                const contract = yield tezos.wallet.at(addMinterDto.fa2ContractAddress);
                const op = yield contract.methods.add_minter(addMinterDto.minter).send();
                yield op.confirmation();
                return op.opHash;
            }
            catch (error) {
                console.log(`Error: ${JSON.stringify(error, null, 2)}`);
            }
        });
    }
    toHexString(input) {
        return Buffer.from(input).toString('hex');
    }
}
exports.default = new TezosService();
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidGV6b3Muc2VydmljZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uL3Rlem9zL3NlcnZpY2VzL3Rlem9zLnNlcnZpY2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztBQUdBLDhDQUFnRDtBQUNoRCw4Q0FBZ0Q7QUFDaEQsNENBQWlEO0FBQ2pELCtHQUF3STtBQUN4SSwyR0FBa0k7QUFDbEksK0NBQWdDO0FBRWhDLE1BQU0sWUFBWTtJQUVSLGNBQWMsQ0FBQyxrQkFBeUM7O1lBQzFELE1BQU0sQ0FBQyxNQUFNLEVBQUUsQ0FBQTtZQUNmLElBQUc7Z0JBQ0MsSUFBSSxNQUFNLENBQUM7Z0JBQ1gsSUFBRyxrQkFBa0IsQ0FBQyxLQUFLLElBQUksT0FBTyxFQUFDO29CQUNuQyxNQUFNLEdBQUUsT0FBTyxDQUFDLEdBQUcsQ0FBQyxlQUFlLENBQUE7aUJBQ3RDO3FCQUFLLElBQUcsa0JBQWtCLENBQUMsS0FBSyxJQUFJLFVBQVUsRUFBQztvQkFDNUMsTUFBTSxHQUFHLE9BQU8sQ0FBQyxHQUFHLENBQUMsZ0JBQWdCLENBQUE7aUJBQ3hDO3FCQUFJO29CQUNELE1BQU0sSUFBSSxLQUFLLENBQUMsZ0NBQWdDLENBQUMsQ0FBQztpQkFDckQ7Z0JBQ0QsTUFBTSxLQUFLLEdBQUcsSUFBSSxzQkFBWSxDQUFDLE1BQU8sQ0FBQyxDQUFDO2dCQUN4QyxLQUFLLENBQUMsaUJBQWlCLENBQUMsSUFBSSx1QkFBYyxDQUFDLE9BQU8sQ0FBQyxHQUFHLENBQUMsV0FBWSxDQUFDLENBQUMsQ0FBQztnQkFDdEUsSUFBSSxJQUFJLENBQUM7Z0JBQ1QsSUFBSSxVQUFVLENBQUM7Z0JBQ2YsSUFBRyxrQkFBa0IsQ0FBQyxJQUFJLElBQUksUUFBUSxFQUFDO29CQUNuQyxJQUFJLEdBQUUsd0NBQWdCLENBQUM7b0JBQ3ZCLFVBQVUsR0FBRSw4Q0FBc0IsQ0FBQTtpQkFDckM7cUJBQUssSUFBRyxrQkFB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