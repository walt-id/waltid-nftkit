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
  async verifySignature(req: express.Request, res: express.Response) {
    // try {
    //

    //   const account = await NearService.verifySignature(
    //     message,
    //     req.query.publicKey,
    //     req.query.signature
    //   );
    //   console.log(account);
    //   res.status(201);
    // } catch (error) {
    //   console.log(error);
    //   res.status(201).send(false);
    // }

    try {
      const message = req.query.message;
      const pk =req.query.publicKey;
      const sig = req.query.signature;

      console.log(message , pk , sig)
      const account = await NearService.verifySignature(
          message,
          req.query.publicKey,
          req.query.signature
      );
      console.log(account);
      res.status(201).send(true);
    } catch (error) {
      console.log(error);
      res.status(201).send(false);
    }
  }
}

export default new NearController();
