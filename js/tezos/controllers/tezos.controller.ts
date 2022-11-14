import express from 'express';
import tezosService from '../services/tezos.service'

class TezosController {

    async deployContract(req: express.Request, res: express.Response) {
        const contractAddress = await tezosService.deployContract(req.body);
        res.status(201).send({ contractAddress: contractAddress });
    }
}

export default new TezosController();
