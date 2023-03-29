import express from "express";
import FlowService from "../services/flow.service";

class FlowController {
  async test(req: express.Request, res: express.Response) {
    console.log("test");
    res.send("test");
  }
  async init(req: express.Request, res: express.Response) {
    console.log("init");
  }

  async accountDetails(req: express.Request, res: express.Response) {
    const account = await FlowService.getAccountDetails();
    console.log(account);
    res.send(account);
  }

  async getNFTs(req: express.Request, res: express.Response) {
    const account = await FlowService.getNftsByAddress(req.body)
    console.log(account);
    res.send(account);
  }
}

export default new FlowController();