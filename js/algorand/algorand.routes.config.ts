import {CommonRoutesConfig} from '../common/common.routes.config';
import express from 'express';
import AlgorandController from "./controllers/algorand.controller";


export class AlgorandRoutes extends CommonRoutesConfig{
    constructor(app: express.Application) {
        super(app, 'AlgorandRoutes');
    }

    configureRoutes() {



        this.app
            .route(`/algorand/signature/verification`)
            .get(
                AlgorandController.verifySignature
            );

        return this.app;
    }
}
