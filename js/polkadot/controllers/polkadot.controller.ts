import express from "express";
import PolkadotService from "../services/polkadot.service";


class PolkadotController {
    async test(req: express.Request, res: express.Response) {
     // @ts-ignore
        const account = await PolkadotService.verifySignature(req.query.message, req.query.signature, req.query.publicKey);

      return res.status(200).send(account);
    }
}
export default new PolkadotController();