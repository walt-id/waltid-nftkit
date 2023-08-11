import * as dotenv from "dotenv";
import algosdk from "algosdk";


dotenv.config();
class AlgorandService{


    async verifySignature(message: string, signature: string, address: string) {

        const _message = new Uint8Array(Buffer.from(message));
        const _signature = new Uint8Array(signature.split(',').map(Number))

        // @ts-ignore
        return algosdk.verifyBytes(_message, _signature, address);

    }
}


export default new AlgorandService();
