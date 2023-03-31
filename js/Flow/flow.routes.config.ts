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

        this.app
            .route(`/flow/getAccountsCollection`)
            .post(
                FlowController.getAccountsCollection
            );

        this.app
            .route(`/flow/getNFTById`)
            .post(
                FlowController.getNFTById
            );

        this.app
            .route(`/flow/NFTinCollection`)
            .get(
                FlowController.getNFTinCollection
            );

        return this.app;
    }
}
