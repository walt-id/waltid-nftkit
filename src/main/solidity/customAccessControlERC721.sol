// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/Pausable.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";

contract CustomAccessControlERC721 is ERC721URIStorage, Pausable, AccessControl  {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    bool public tokensBurnable;
    //bool public metadataUpdatable;
    bool public tokensTransferable;

    bytes32 public constant MINTER_ROLE = keccak256("MINTER_ROLE");
    bytes32 public constant METADATA_UPDATER_ROLE = keccak256("METADATA_UPDATER_ROLE");


    constructor(string memory name, string memory symbol, bool _tokensBurnable, bool _tokensTransferable) ERC721(name, symbol) {
        tokensBurnable = _tokensBurnable;
        tokensTransferable = _tokensTransferable;

        _setupRole(DEFAULT_ADMIN_ROLE, msg.sender);
        _setRoleAdmin(MINTER_ROLE, DEFAULT_ADMIN_ROLE);
        _setRoleAdmin(METADATA_UPDATER_ROLE, DEFAULT_ADMIN_ROLE);
    }

     function mintTo(address _recipient, string memory tokenURI) public onlyRole(MINTER_ROLE) returns (uint256) {
        _tokenIds.increment();

        uint256 newItemId = _tokenIds.current();
        _mint(_recipient, newItemId);
        _setTokenURI(newItemId, tokenURI);

        return newItemId;
    }

    function setTokenURI(uint256 tokenId, string memory tokenURI) public onlyRole(METADATA_UPDATER_ROLE){
            _setTokenURI(tokenId, tokenURI);
    }

    function setTransferableOption(bool _tokensTransferable) external onlyRole(DEFAULT_ADMIN_ROLE) {
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
        if (from != address(0) && to != address(0)) {
            require(tokensTransferable, "NFT: tokens transfer is disabled");
        }
    }

    function setBurnableOption(bool _tokensBurnable) external onlyRole(DEFAULT_ADMIN_ROLE) {
        tokensBurnable = _tokensBurnable;
    }

    function burn(uint256 tokenId) public virtual {
        require(tokensBurnable, "NFT: tokens burn is disabled");
        require(_isApprovedOrOwner(_msgSender(), tokenId), "ERC721Burnable: caller is not owner nor approved");
        _burn(tokenId);
    }

       function pause() public onlyRole(DEFAULT_ADMIN_ROLE) {
        _pause();
    }

    function unpause() public onlyRole(DEFAULT_ADMIN_ROLE) {
        _unpause();
    }

    function supportsInterface(bytes4 interfaceId) public view virtual override(ERC721, AccessControl) returns (bool) {
        return super.supportsInterface(interfaceId);
    }

}