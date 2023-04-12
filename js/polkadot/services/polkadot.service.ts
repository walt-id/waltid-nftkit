
import * as dotenv from "dotenv";
const { cryptoWaitReady, decodeAddress, signatureVerify } = require('@polkadot/util-crypto');
const { u8aToHex } = require('@polkadot/util');


dotenv.config();

class PolkadotService{

    async verifySignature(signedMessage: string, signature: string, address: string){
        //@ts-ignore
        const isValidSignature = (signedMessage, signature, address) => {
            const publicKey = decodeAddress(address);
            const hexPublicKey = u8aToHex(publicKey);

            return signatureVerify(signedMessage, signature, hexPublicKey).isValid;
        };

        const main = async () => {
            //Some interfaces, such as using sr25519 however are only available via WASM
            await cryptoWaitReady();
            const isValid = isValidSignature(
                signedMessage,
                signature,
                address
            );
            console.log(isValid)
            // true
        }
        main().catch(console.error);

    }

}
export default new PolkadotService();