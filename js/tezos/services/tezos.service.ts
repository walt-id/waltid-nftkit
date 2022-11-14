import { ContractDeploymentDto} from '../dto/deploy.contract.dto'
import { TezosToolkit } from '@taquito/taquito';
import { InMemorySigner } from '@taquito/signer';
import {code as multipleNftFa2Code, getStorage as multipleNftFa2GetStorage} from '../smart contract/multiple-nft-fa2-private-collection'
import {code as singleNftFa2Code, getStorage as singleNftFa2GetStorage} from '../smart contract/single-nft-fa2-private-collection'
import * as dotenv from 'dotenv'

class TezosService {

    async deployContract(contractDeployment: ContractDeploymentDto) {
        dotenv.config()
        try{
            let rpc_url;
            if(contractDeployment.chain == "TEZOS"){
                rpc_url= process.env.MAINNET_RPC_URL
            }else if(contractDeployment.chain == "GHOSTNET"){
                rpc_url = process.env.GHOSTNET_RPC_URL
            }else{
                throw new Error('Chain parameter is not defined');
            }
            const tezos = new TezosToolkit(rpc_url!);
            tezos.setSignerProvider(new InMemorySigner(process.env.PRIVATE_KEY!));
            let code;
            let getStorage;
            if(contractDeployment.type == "SINGLE"){
                code= singleNftFa2Code;
                getStorage= singleNftFa2GetStorage
            }else if(contractDeployment.type == "MULTIPLE"){
                code= multipleNftFa2Code;
                getStorage= multipleNftFa2GetStorage
            }else{
                throw new Error('Type parameter is not defined');
            }
            const operation = await tezos.wallet.originate({
                code: code,
                init: getStorage(contractDeployment.owner)
            }).send();
            console.log(`Waiting for confirmation of origination...`);
            const contract = await operation.contract();
            console.log(`Origination completed for ${contract.address}.`);
            return contract.address
        } catch (error) {
            console.log(`Error: ${JSON.stringify(error, null, 2)}`);
        }
    }
}
export default new TezosService();
