import express from "express";
import AlgorandService from "../services/algorand.service";




class AlgorandController {


    async verifySignature(req: express.Request, res: express.Response) {

        const account = await AlgorandService.verifySignature(
            //@ts-ignore
            req.query.message,
            req.query.signature,
            req.query.publicKey
        );
        console.log(account);
        res.status(201).send(account);
    }

}
export default new AlgorandController();
