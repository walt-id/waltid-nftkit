import { CommonRoutesConfig } from "../common/common.routes.config";
import express from "express";
import PolkadotController from "./controllers/polkadot.controller";
export class PolkadotRoutes extends CommonRoutesConfig {
  constructor(app: express.Application) {
    super(app, "PolkadotRoutes");
  }

  configureRoutes() {
    this.app.route(`/polkadot/test`).get(PolkadotController.test);
    
    return this.app;
  }
}
