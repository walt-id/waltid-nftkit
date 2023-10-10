// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/token/ERC721/ERC721.sol";

contract waltidSoulBound is ERC721,ERC721URIStorage , Ownable{

    using Counters for Counters.Counter;
    Counters.Counter private _tokenIdCounter;

    constructor() ERC721("WaltidSBT","SBT") {}

    function safeMint(address to , string memory uri) public onlyOwner {

        uint256 tokenId = _tokenIdCounter.current();
        _tokenIdCounter.increment();
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, uri);

    }


    function _burn(uint256 tokenId) internal override(ERC721, ERC721URIStorage) {
        super._burn(tokenId);
    }

    function unequip(uint256 tokenId) external  {
        require(ownerOf(tokenId) == msg.sender , "You are not the owner of this NFT");
        _burn(tokenId);
    }

    function revoke(uint256 tokenId) external onlyOwner{
        _burn(tokenId);
    }

    function tokenURI(uint256 tokenId)
    public
    view
    override(ERC721, ERC721URIStorage)
    returns (string memory)
    {
        return super.tokenURI(tokenId);
    }

    function _beforeTokenTransfer(address from, address to, uint256 tokenId) internal override virtual {
        require(from == address(0) || to == address(0), "Err: token transfer is BLOCKED");
        super._beforeTokenTransfer(from, to, tokenId);
    }

}