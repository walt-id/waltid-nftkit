import {CommonRoutesConfig} from '../common/common.routes.config';
import express from 'express';
import NearController from  '../near/controllers/near.controller';
export class NearRoutes extends CommonRoutesConfig {
    constructor(app: express.Application) {
        super(app, 'NearRoutes');
    }

    configureRoutes() {
        
        this.app
        .route(`/near/accountDetails`)
        .post(
            NearController.accountDetails
        );
        this.app
        .route(`/near/sub-account/create`)
        .post(
            NearController.createAccount
            
        );
        this.app
        .route(`/near/contract/deploywithdefaultmetadata`)
        .post(
            NearController.deployContract
            
        );

        this.app
        .route(`/near/contract/deploywithcustommetadata`)
        .post(
            NearController.deployContractWithCustomMetadata

        );
        this.app
        .route(`/near/contract/mintToken`)
        .post(
            NearController.mintToken
            
        );
        return this.app;
    }
}
