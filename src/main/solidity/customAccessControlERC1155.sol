// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/Pausable.sol";
import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";



contract CustomAccessControlERC1155 is ERC1155, AccessControl, Pausable, ERC1155Burnable  {

    mapping(uint256 => string) private _tokenURIs;
    string private _baseURI = "";


    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    bool private tokensBurnable;
    //bool private metadataUpdatable;
    bool private tokensTransferable;

    bytes32 public constant URI_SETTER_ROLE = keccak256("URI_SETTER_ROLE");
    bytes32 public constant MINTER_ROLE = keccak256("MINTER_ROLE");


    constructor ( bool _tokensBurnable, bool _tokensTransferable) ERC1155 ("") {
        tokensBurnable = _tokensBurnable;
        tokensTransferable = _tokensTransferable;

        _setupRole(DEFAULT_ADMIN_ROLE, msg.sender);
        _setRoleAdmin(MINTER_ROLE, DEFAULT_ADMIN_ROLE);
        _setRoleAdmin(URI_SETTER_ROLE, DEFAULT_ADMIN_ROLE);

    }

    function uri(uint256 tokenId) public override view returns (string memory) {
        string memory tokenURI = _tokenURIs[tokenId];

        // If token URI is set, concatenate base URI and tokenURI (via abi.encodePacked).
        return bytes(tokenURI).length > 0 ? string(abi.encodePacked(_baseURI, tokenURI)) : super.uri(tokenId);
    }

    function setURI(uint256 tokenId, string memory tokenURI) public onlyRole(URI_SETTER_ROLE) {
        _setURI(tokenId,tokenURI);
    }

    function _setURI(uint256 tokenId, string memory tokenURI) internal virtual {
        _tokenURIs[tokenId] = tokenURI;
        emit URI(uri(tokenId), tokenId);
    }

    function mint(address account, uint256 amount, string memory tokenURI, bytes memory data) public onlyRole(MINTER_ROLE)
    {
            _tokenIds.increment();
            uint256 newItemId = _tokenIds.current();
            _mint(account, newItemId, amount, data);
            _setURI(newItemId,tokenURI);

    }

   function mintBatch(address to, uint256[] memory ids, uint256[] memory amounts, bytes memory data)
        external onlyRole(DEFAULT_ADMIN_ROLE)
    {
        _mintBatch(to, ids, amounts, data);
    }

    function setTransferableOption(bool _tokensTransferable) external onlyRole(DEFAULT_ADMIN_ROLE){
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
    {
            super._beforeTokenTransfer(operator, from, to, ids, amounts, data);
            require(!paused(), "ERC1155Pausable: token transfer while paused");
    }


    function setBurnableOption(bool _tokensBurnable) external onlyRole(DEFAULT_ADMIN_ROLE) {
        tokensBurnable = _tokensBurnable;
    }

    function getBurnableOption () external view returns (bool) {

        return  tokensBurnable;
    }

    function pause() public onlyRole(DEFAULT_ADMIN_ROLE) {
        _pause();
    }

    function unpause() public onlyRole(DEFAULT_ADMIN_ROLE) {
        _unpause();
    }

    function _setBaseURI(string memory baseURI) internal virtual {
        _baseURI = baseURI;
    }


    function supportsInterface(bytes4 interfaceId)
        public
        view
        override(ERC1155, AccessControl)
        returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }
}