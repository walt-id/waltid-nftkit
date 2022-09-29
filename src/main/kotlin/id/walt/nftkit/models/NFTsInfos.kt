package id.walt.nftkit.models

import id.walt.nftkit.services.NFTsAlchemyResult
import id.walt.nftkit.services.TezosNFTsTzktResult

class NFTsInfos(
    val evmNfts: List<NFTsAlchemyResult.NftTokenByAlchemy>?= null,
    val tezosNfts: List<TezosNFTsTzktResult>?= null
) {
}