import {CommonRoutesConfig} from '../common/common.routes.config';
import express from 'express';
import FlowController from "../Flow/controllers/flow.controller";


export class AlgorandRoutes extends CommonRoutesConfig{
    constructor(app: express.Application) {
        super(app, 'AlgorandRoutes');
    }

    configureRoutes() {



        this.app
            .route(`/algorand/signature/verification`)
            .post(
               //TODO
            );

        return this.app;
    }
}