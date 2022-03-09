// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/access/Ownable.sol";


contract CredentialNFT is ERC721URIStorage , Ownable  {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

  constructor(string memory name, string memory symbol) ERC721(name, symbol) {
  }

  //The recipient is the subject of the VC
     function mintTo(address _recipient, string memory tokenURI) public  returns (uint256) {
        _tokenIds.increment();

        uint256 newItemId = _tokenIds.current();
        _mint(_recipient, newItemId);
        _setTokenURI(newItemId, tokenURI);

        return newItemId;
    }

    function tokenURI(uint256 tokenId) public view virtual override returns (string memory) {
      require(_exists(tokenId), "ERC721URIStorage: URI query for nonexistent token");
      string memory _tokenURI = super.tokenURI(tokenId);
      return string(abi.encodePacked("data:application/json;base64,",_tokenURI));
   } 

   /**
     * @dev See {IERC721-approve}.
     */
    function approve(address to, uint256 tokenId) public virtual override {
        revert("Nontransferable token");
    }

     /**
     * @dev See {IERC721-safeTransferFrom}.
     */
    function safeTransferFrom(
        address from,
        address to,
        uint256 tokenId
    ) public virtual override {
        revert("Nontransferable token");
    }

    /**
     * @dev See {IERC721-safeTransferFrom}.
     */
    function safeTransferFrom(
        address from,
        address to,
        uint256 tokenId,
        bytes memory _data
    ) public virtual override {
        revert("Nontransferable token");
    }

     /**
     * @dev Approve `operator` to operate on all of `owner` tokens
     *
     * Emits a {ApprovalForAll} event.
     */
    function _setApprovalForAll(
        address owner,
        address operator,
        bool approved
    ) internal virtual override{
        revert("Nontransferable token");
    }

      /**
     * @dev See {IERC721-transferFrom}.
     */
    function transferFrom(
        address from,
        address to,
        uint256 tokenId
    ) public virtual override {
        revert("Nontransferable token");
    }

}