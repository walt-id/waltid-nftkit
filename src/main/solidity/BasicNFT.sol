// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
//import "C:/Users/we.khemiri/Desktop/assur/id/NFTs/node_modules/@openzeppelin/contracts/token/ERC721/ERC721.sol";
//import "C:/Users/we.khemiri/Desktop/assur/id/NFTs/node_modules/@openzeppelin/contracts/utils/Counters.sol";
contract BasicNFTs is ERC721 {
    using Counters for Counters.Counter;
    Counters.Counter private currentTokenId;

    constructor() ERC721("Metaverse Tokens", "METT") {}

    function mintTo(address recipient)
        public
        returns (uint256)
    {
        currentTokenId.increment();
        uint256 newItemId = currentTokenId.current();
        _safeMint(recipient, newItemId);
        return newItemId;
    }
}