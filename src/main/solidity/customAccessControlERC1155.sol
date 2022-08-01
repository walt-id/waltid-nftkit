// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/Pausable.sol";
import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155URIStorage.sol";



contract CustomAccessControlERC1155 is ERC1155, ERC1155URIStorage, AccessControl, Pausable, ERC1155Burnable  {

    mapping(uint256 => string) private _tokenURIs;
    string private _baseURI = "";


    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    bool public tokensBurnable;
    //bool public metadataUpdatable;
    bool public tokensTransferable;

    bytes32 public constant URI_SETTER_ROLE = keccak256("URI_SETTER_ROLE");
    bytes32 public constant PAUSER_ROLE = keccak256("PAUSER_ROLE");
    bytes32 public constant MINTER_ROLE = keccak256("MINTER_ROLE");



     constructor (string memory uri, bool _tokensBurnable, bool _tokensTransferable) ERC1155 (uri) {
        tokensBurnable = _tokensBurnable;
        tokensTransferable = _tokensTransferable;

        _setupRole(DEFAULT_ADMIN_ROLE, msg.sender);
        _setupRole(URI_SETTER_ROLE, msg.sender);
        _setupRole(MINTER_ROLE, msg.sender);

    }

    function uri(uint256 tokenId) public view virtual override returns (string memory) {
        string memory tokenURI = _tokenURIs[tokenId];

        // If token URI is set, concatenate base URI and tokenURI (via abi.encodePacked).
        return bytes(tokenURI).length > 0 ? string(abi.encodePacked(_baseURI, tokenURI)) : super.uri(tokenId);
    }

    function setTokenUri(uint256 id, string memory uri) public onlyRole(URI_SETTER_ROLE) {
        _setURI(id,uri);
    }


    function mint(address account, uint256 amount, string memory uri, bytes memory data) public onlyRole(MINTER_ROLE)
    {
            _tokenIds.increment();
            uint256 newItemId = _tokenIds.current();
            _mint(account, newItemId, amount, data);
            _setURI(newItemId,uri);
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
     //whenNotPaused
        //override
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


    function supportsInterface(bytes4 interfaceId)
        public
        view
        override(ERC1155, AccessControl)
        returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }
}