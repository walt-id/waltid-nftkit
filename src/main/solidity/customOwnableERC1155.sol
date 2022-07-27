// SPDX-License-Identifier: MIT
pragma solidity >=0.7.0 <0.9.0;


import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/Pausable.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";


contract CustomOwnable is ERC1155, Ownable, Pausable, ERC1155Burnable {

    mapping (uint256 => string) private _tokenURIs;

    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    bool private tokensBurnable;
    bool private tokensTransferable;


    constructor (string memory uri, bool _tokensBurnable, bool _tokensTransferable) ERC1155 (uri) {
        tokensBurnable = _tokensBurnable;
        tokensTransferable = _tokensTransferable;

    }

   function uris(uint256 id)  public view returns (string memory) {
        return(_tokenURIs[id]);
    }

    function _setTokenUri(uint256 id, string memory uri) private {
         _tokenURIs[id] = uri;
    }


    function mint(address account, uint256 amount, string memory uri, bytes memory data) public onlyOwner
        {
            _tokenIds.increment();
            uint256 newItemId = _tokenIds.current();
            _mint(account, newItemId, amount, data);
            _setTokenUri(newItemId,uri);
        }

    function mintBatch(address to, uint256[] memory ids, uint256[] memory amounts, bytes memory data)
        public
        onlyOwner
    {
        _mintBatch(to, ids, amounts, data);
    }

    function setTransferableOption(bool _tokensTransferable) external onlyOwner {
        tokensTransferable = _tokensTransferable;
    }

    function getTransferableOprion () external view returns (bool)
    {
        return tokensTransferable;
    }

    function _beforeTokenTransfer(
        address operator,
        address from,
        address to,
        uint256[] memory ids,
        uint256[] memory amounts,
        bytes memory data
    ) internal virtual override
     //whenNotPaused
        //override
    {
            super._beforeTokenTransfer(operator, from, to, ids, amounts, data);
            require(!paused(), "ERC1155Pausable: token transfer while paused");
    }


    function setBurnableOption(bool _tokensBurnable) external onlyOwner {
        tokensBurnable = _tokensBurnable;
    }

    function getBurnableOption () external view returns (bool) {

        return  tokensBurnable;
    }



    function pause() public onlyOwner {
        _pause();
    }

    function unpause() public onlyOwner {
        _unpause();
    }


}