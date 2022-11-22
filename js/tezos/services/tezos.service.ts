import { ContractDeploymentDto} from '../dto/deploy.contract.dto'
import{ MintTokenDto } from '../dto/mint.token.dto'
import {AddMinterDto} from '../dto/add.minter.dto'
import { TezosToolkit } from '@taquito/taquito';
import { MichelsonMap } from '@taquito/taquito';
import { InMemorySigner } from '@taquito/signer';
import {code as multipleNftFa2Code, getStorage as multipleNftFa2GetStorage} from '../smart contract/multiple-nft-fa2-private-collection'
import {code as singleNftFa2Code, getStorage as singleNftFa2GetStorage} from '../smart contract/single-nft-fa2-private-collection'
import * as dotenv from 'dotenv'

class TezosService {

    async deployContract(contractDeployment: ContractDeploymentDto) {
        dotenv.config()
        try{
            let rpcUrl;
            if(contractDeployment.chain == "TEZOS"){
                rpcUrl= process.env.MAINNET_RPC_URL
            }else if(contractDeployment.chain == "GHOSTNET"){
                rpcUrl = process.env.GHOSTNET_RPC_URL
            }else{
                throw new Error('Chain parameter is not defined');
            }
            const tezos = new TezosToolkit(rpcUrl!);
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

    async mintToken(mintTokenDto: MintTokenDto){
        try{
            dotenv.config()
            let rpcUrl;
            if(mintTokenDto.chain == "TEZOS"){
                rpcUrl= process.env.MAINNET_RPC_URL
            }else if(mintTokenDto.chain == "GHOSTNET"){
                rpcUrl = process.env.GHOSTNET_RPC_URL
            }else{
                throw new Error('Chain parameter is not defined');
            }
            const tezos = await new TezosToolkit(rpcUrl!);
            tezos.setSignerProvider(new InMemorySigner(process.env.PRIVATE_KEY!));
            const contract = await tezos.wallet.at(mintTokenDto.fa2ContractAddress);
            const token_info = new MichelsonMap<string, string>();
            token_info.set('', this.toHexString(mintTokenDto.metadata));
            const royalties: any[] = [];
            let op;
            if(mintTokenDto.amount){
                op = await contract.methods.mint(mintTokenDto.tokenId, mintTokenDto.owner, mintTokenDto.amount,token_info, royalties).send();
            }else{
                op = await contract.methods.mint(mintTokenDto.tokenId, mintTokenDto.owner, token_info, royalties).send();
            }
            await op.confirmation();
            return op.opHash
        } catch (error) {
            console.log(`Error: ${JSON.stringify(error, null, 2)}`);
        }
    }

    async addMinter(addMinterDto: AddMinterDto){
        try{
            dotenv.config()
            let rpcUrl;
            if(addMinterDto.chain == "TEZOS"){
                rpcUrl= process.env.MAINNET_RPC_URL
            }else if(addMinterDto.chain == "GHOSTNET"){
                rpcUrl = process.env.GHOSTNET_RPC_URL
            }else{
                throw new Error('Chain parameter is not defined');
            }
            const tezos = await new TezosToolkit(rpcUrl!);
            tezos.setSignerProvider(new InMemorySigner(process.env.PRIVATE_KEY!));
            const contract = await tezos.wallet.at(addMinterDto.fa2ContractAddress);
            const op = await contract.methods.add_minter(addMinterDto.minter).send();
            await op.confirmation();
            return op.opHash
        } catch (error) {
            console.log(`Error: ${JSON.stringify(error, null, 2)}`);
        }
    }

    toHexString(input: string) {
        return Buffer.from(input).toString('hex');
    }
}
export default new TezosService();
