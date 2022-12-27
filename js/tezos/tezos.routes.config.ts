import {CommonRoutesConfig} from '../common/common.routes.config';
import TezosController from '../tezos/controllers/tezos.controller'
import express from 'express';

export class TezosRoutes extends CommonRoutesConfig {
    constructor(app: express.Application) {
        super(app, 'TezosRoutes');
    }

    configureRoutes() {
        this.app
        .route(`/tezos/contract/deployment`)
        .post(
            TezosController.deployContract
        );
        this.app
        .route(`/tezos/contract/token/mint`)
        .post(
            TezosController.mintToken
        );
        this.app
        .route(`/tezos/contract/minter`)
        .post(
            TezosController.addMinter
        );
        this.app
        .route(`/tezos/signature/verification`)
        .get(
            TezosController.verifySignature
        );
        return this.app;
    }
}