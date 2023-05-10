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
            .route(`/flow/getNFTsInCollection`)
            .post(
                FlowController.getNFTsByAddressInCollection
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
            .route(`/flow/getContractName`)
            .get(
                FlowController.getContractName
            );

        this.app
            .route(`/flow/getIDScript`)
            .get(
                FlowController.getIDScript
            );

        return this.app;
    }
}
