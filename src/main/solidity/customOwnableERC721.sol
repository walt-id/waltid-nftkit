// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/Pausable.sol";



contract CustomOwnableERC721 is ERC721URIStorage, Ownable, Pausable  {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    bool public tokensBurnable;
    //bool public metadataUpdatable;
    bool public tokensTransferable;




    constructor(string memory name, string memory symbol, bool _tokensBurnable, bool _tokensTransferable) ERC721(name, symbol) {
        tokensBurnable = _tokensBurnable;
        tokensTransferable = _tokensTransferable;
    }

     function mintTo(address _recipient, string memory tokenURI) public onlyOwner  returns (uint256) {
        _tokenIds.increment();

        uint256 newItemId = _tokenIds.current();
        _mint(_recipient, newItemId);
        _setTokenURI(newItemId, tokenURI);

        return newItemId;
    }

    function setTransferableOption(bool _tokensTransferable) external onlyOwner {
        tokensTransferable = _tokensTransferable;
    }

       /// @dev See {ERC721-_beforeTokenTransfer}.
    function _beforeTokenTransfer(
        address from,
        address to,
        uint256 tokenId
    ) internal virtual override {
        super._beforeTokenTransfer(from, to, tokenId);

        require(!paused(), "ERC721Pausable: token operations while paused");
        // if transfer is restricted on the contract, we still want to allow burning and minting
        if (/*!hasRole(TRANSFER_ROLE, address(0)) && */from != address(0) && to != address(0)) {
            require(tokensTransferable, "NFT: tokens transfer is disabled");
        }
    }

    function setBurnableOption(bool _tokensBurnable) external onlyOwner{
        tokensBurnable = _tokensBurnable;
    }

       /**
     * @dev Burns `tokenId`. See {ERC721-_burn}.
     *
     * Requirements:
     *
     * - The caller must own `tokenId` or be an approved operator.
     */
    function burn(uint256 tokenId) public virtual {
        require(tokensBurnable, "NFT: tokens burn is disabled");
        require(_isApprovedOrOwner(_msgSender(), tokenId), "ERC721Burnable: caller is not owner nor approved");
        _burn(tokenId);
    }

       function pause() public onlyOwner {
        _pause();
    }

    function unpause() public onlyOwner {
        _unpause();
    }

}