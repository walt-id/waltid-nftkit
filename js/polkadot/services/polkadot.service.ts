
import * as dotenv from "dotenv";
const { cryptoWaitReady, decodeAddress, signatureVerify } = require('@polkadot/util-crypto');
const { u8aToHex } = require('@polkadot/util');


dotenv.config();

class PolkadotService{

    async verifySignature(signedMessage: string, signature: string, publicKey: string){
        //@ts-ignore

        const pK = decodeAddress(publicKey);
        const hexPublicKey = u8aToHex(pK);

        await cryptoWaitReady();

        return signatureVerify(signedMessage, signature, hexPublicKey).isValid;




    }

}
export default new PolkadotService();