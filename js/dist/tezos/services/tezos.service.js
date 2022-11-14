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
const signer_1 = require("@taquito/signer");
const multiple_nft_fa2_private_collection_1 = require("../smart contract/multiple-nft-fa2-private-collection");
const single_nft_fa2_private_collection_1 = require("../smart contract/single-nft-fa2-private-collection");
const dotenv = __importStar(require("dotenv"));
class TezosService {
    deployContract(contractDeployment) {
        return __awaiter(this, void 0, void 0, function* () {
            dotenv.config();
            try {
                let rpc_url;
                if (contractDeployment.chain == "TEZOS") {
                    rpc_url = process.env.MAINNET_RPC_URL;
                }
                else if (contractDeployment.chain == "GHOSTNET") {
                    rpc_url = process.env.GHOSTNET_RPC_URL;
                }
                else {
                    throw new Error('Chain parameter is not defined');
                }
                const tezos = new taquito_1.TezosToolkit(rpc_url);
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
}
exports.default = new TezosService();
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidGV6b3Muc2VydmljZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uL3Rlem9zL3NlcnZpY2VzL3Rlem9zLnNlcnZpY2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztBQUNBLDhDQUFnRDtBQUNoRCw0Q0FBaUQ7QUFDakQsK0dBQXdJO0FBQ3hJLDJHQUFrSTtBQUNsSSwrQ0FBZ0M7QUFFaEMsTUFBTSxZQUFZO0lBRVIsY0FBYyxDQUFDLGtCQUF5Qzs7WUFDMUQsTUFBTSxDQUFDLE1BQU0sRUFBRSxDQUFBO1lBQ2YsSUFBRztnQkFDQyxJQUFJLE9BQU8sQ0FBQztnQkFDWixJQUFHLGtCQUFrQixDQUFDLEtBQUssSUFBSSxPQUFPLEVBQUM7b0JBQ25DLE9BQU8sR0FBRSxPQUFPLENBQUMsR0FBRyxDQUFDLGVBQWUsQ0FBQTtpQkFDdkM7cUJBQUssSUFBRyxrQkFBa0IsQ0FBQyxLQUFLLElBQUksVUFBVSxFQUFDO29CQUM1QyxPQUFPLEdBQUcsT0FBTyxDQUFDLEdBQUcsQ0FBQyxnQkFBZ0IsQ0FBQTtpQkFDekM7cUJBQUk7b0JBQ0QsTUFBTSxJQUFJLEtBQUssQ0FBQyxnQ0FBZ0MsQ0FBQyxDQUFDO2lCQUNyRDtnQkFDRCxNQUFNLEtBQUssR0FBRyxJQUFJLHNCQUFZLENBQUMsT0FBUSxDQUFDLENBQUM7Z0JBQ3pDLEtBQUssQ0FBQyxpQkFBaUIsQ0FBQyxJQUFJLHVCQUFjLENBQUMsT0FBTyxDQUFDLEdBQUcsQ0FBQyxXQUFZLENBQUMsQ0FBQyxDQUFDO2dCQUN0RSxJQUFJLElBQUksQ0FBQztnQkFDVCxJQUFJLFVBQVUsQ0FBQztnQkFDZixJQUFHLGtCQUFrQixDQUFDLElBQUksSUFBSSxRQUFRLEVBQUM7b0JBQ25DLElBQUksR0FBRSx3Q0FBZ0IsQ0FBQztvQkFDdkIsVUFBVSxHQUFFLDhDQUFzQixDQUFBO2lCQUNyQztxQkFBSyxJQUFHLGtCQUFrQixDQUFDLElBQUksSUFBSSxVQUFVLEVBQUM7b0JBQzNDLElBQUksR0FBRSwwQ0FBa0IsQ0FBQztvQkFDekIsVUFBVSxHQUFFLGdEQUF3QixDQUFBO2lCQUN2QztxQkFBSTtvQkFDRCxNQUFNLElBQUksS0FBSyxDQUFDLCtCQUErQixDQUFDLENBQUM7aUJBQ3BEO2dCQUNELE1BQU0sU0FBUyxHQUFHLE1BQU0sS0FBSyxDQUFDLE1BQU0sQ0FBQyxTQUFTLENBQUM7b0JBQzNDLElBQUksRUFBRSxJQUFJO29CQUNWLElBQUksRUFBRSxVQUFVLENBQUMsa0JBQWtCLENBQUMsS0FBSyxDQUFDO2lCQUM3QyxDQUFDLENBQUMsSUFBSSxFQUFFLENBQUM7Z0JBQ1YsT0FBTyxDQUFDLEdBQUcsQ0FBQyw0Q0FBNEMsQ0FBQyxDQUFDO2dCQUMxRCxNQUFNLFFBQVEsR0FBRyxNQUFNLFNBQVMsQ0FBQyxRQUFRLEVBQUUsQ0FBQztnQkFDNUMsT0FBTyxDQUFDLEdBQUcsQ0FBQyw2QkFBNkIsUUFBUSxDQUFDLE9BQU8sR0FBRyxDQUFDLENBQUM7Z0JBQzlELE9BQU8sUUFBUSxDQUFDLE9BQU8sQ0FBQTthQUMxQjtZQUFDLE9BQU8sS0FBSyxFQUFFO2dCQUNaLE9BQU8sQ0FBQyxHQUFHLENBQUMsVUFBVSxJQUFJLENBQUMsU0FBUyxDQUFDLEtBQUssRUFBRSxJQUFJLEVBQUUsQ0FBQyxDQUFDLEVBQUUsQ0FBQyxDQUFDO2FBQzNEO1FBQ0wsQ0FBQztLQUFBO0NBQ0o7QUFDRCxrQkFBZSxJQUFJLFlBQVksRUFBRSxDQUFDIn0=