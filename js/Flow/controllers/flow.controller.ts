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

  async getNFTsByAddress(req: express.Request, res: express.Response) {
    const account = await FlowService.getNftsByAddress(req.body)
    console.log(account);
    res.send(account);
  }

  async getAllNFTs(req: express.Request, res: express.Response) {
    const account = await FlowService.getAllNFTs(req.body)
    console.log(account);
    res.send(account);
  }

  async getAccountsCollection(req: express.Request, res: express.Response) {
    const account = await FlowService.getAccountCollection(req.body)
    console.log(account);
    res.send(account);
  }

  async getNFTById(req: express.Request, res: express.Response) {
    const account = await FlowService.getNftById(req.body.address , req.body.id )
    console.log(account);
    res.send(account);
  }
}

export default new FlowController();