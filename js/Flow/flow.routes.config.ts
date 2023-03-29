import {CommonRoutesConfig} from '../common/common.routes.config';
import express from 'express';
import FlowController from  '../Flow/controllers/flow.controller';
export class FlowRoutes extends CommonRoutesConfig {
    constructor(app: express.Application) {
        super(app, 'FlowRoutes');
    }

    configureRoutes() {

        this.app
            .route(`/flow/accountDetails`)
            .get(
                FlowController.accountDetails
            );
        this.app
            .route(`/flow/getNFTsByAddress`)
            .post(
                FlowController.getNFTsByAddress
            );
        this.app
            .route(`/flow/getAllNFTs`)
            .post(
                FlowController.getAllNFTs
            );

        return this.app;
    }
}
