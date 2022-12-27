import express from 'express';
import tezosService from '../services/tezos.service'
import { char2Bytes } from '@taquito/utils';


class TezosController {

    async deployContract(req: express.Request, res: express.Response) {
        const contractAddress = await tezosService.deployContract(req.body);
        res.status(201).send({ contractAddress: contractAddress });
    }

    async mintToken(req: express.Request, res: express.Response) {
        const result = await tezosService.mintToken(req.body);
        res.status(201).send({opHash: result});
    }

    async addMinter(req: express.Request, res: express.Response) {
        const result = await tezosService.addMinter(req.body);
        res.status(201).send({opHash: result});
    }

    async verifySignature(req: express.Request, res: express.Response) {
        try {
        const message= req.query.message
        if ( typeof message !== "string" ) {
            res.status(201).send(false);
            return
        }
        const bytes = char2Bytes(message);
        const payloadBytes = '05' + '0100' + char2Bytes(bytes.length.toString()) + bytes;
        const isVerified = await tezosService.verifySignature(payloadBytes, req.query.publicKey, req.query.signature);
        console.log("result: " + isVerified)
        res.status(201).send(isVerified);
        } catch (error) {
            res.status(201).send(false);
        }
        
    }
}

export default new TezosController();
