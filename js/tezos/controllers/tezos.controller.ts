import express from 'express';
import tezosService from '../services/tezos.service'

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
}

export default new TezosController();
