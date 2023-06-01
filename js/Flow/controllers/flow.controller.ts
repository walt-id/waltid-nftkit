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

  async getNFTsByAddressInCollection(
    req: express.Request,
    res: express.Response
  ) {
    const account = await FlowService.getNftsByAddressInCollection(
      req.body.account_id,
      req.body.collectionPath,
      req.body.chain
    );
    console.log(account);
    res.send(account);
  }

  async getAllNFTs(req: express.Request, res: express.Response) {
    const account = await FlowService.getAllNFTs(
      req.body.account_id,
      req.body.chain
    );
    console.log(account);
    res.send(account);
  }

  async getAccountsCollection(req: express.Request, res: express.Response) {
    const account = await FlowService.getAccountCollection(req.body);
    console.log(account);
    res.send(account);
  }

  async getNFTById(req: express.Request, res: express.Response) {
    const account = await FlowService.getNftById(
      req.body.account_id,
      req.body.contractAddress,
      req.body.collectionPublicPath,
      req.body.id,
      req.body.chain
    );
    console.log(account);
    res.send(account);
  }

  async getContractName(req: express.Request, res: express.Response) {
    const account = await FlowService.getContractName();
    console.log(account);
  }

  async getIDScript(req: express.Request, res: express.Response) {
    const account = await FlowService.getID_script();
    console.log(account);
  }

  async verifySignature(req: express.Request, res: express.Response) {


    // @ts-ignore
    const account = await FlowService.verifySignature(
        req.query.signature,
        req.query.message

    );

    res.send(account);

  }
}

export default new FlowController();
