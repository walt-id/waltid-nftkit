import express from "express";
import NearService from "../services/near.service";

class NearController {
  async test(req: express.Request, res: express.Response) {
    console.log("test");
    res.send("test");
  }
  async init(req: express.Request, res: express.Response) {
    console.log("init");
  }

  async accountDetails(req: express.Request, res: express.Response) {
    const account = await NearService.getAccountDetails();
    console.log(account);
    res.send(account);
  }

  async createAccount(req: express.Request, res: express.Response) {
    const account = await NearService.createAccount(req.body);
    res.status(201);
    res.send({hash: account });
  }

  async deployContract(req: express.Request, res: express.Response) {
    const account = await NearService.deployContract(req.body);
    res.status(201).send({hash : account });
  }

  async deployContractWithCustomMetadata(
    req: express.Request,
    res: express.Response
  ) {
    const account = await NearService.deployContractWithCustomMetadata(
      req.body
    );
    res.status(201).send({ hash :account });
  }

  async mintToken(req: express.Request, res: express.Response) {
    const account = await NearService.mintToken(req.body);
    res.status(201);
    res.send({hash: account });
  }
}

export default new NearController();
