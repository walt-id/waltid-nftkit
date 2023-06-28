package id.walt.nftkit.models

interface NftDataConverter<in K, out T> {
    fun convert(data: K): T
}

abstract class NftDetailConverterBase<in T> : NftDataConverter<T, NftDetailDataTransferObject>
abstract class NftListConverterBase<in T> : NftDataConverter<T, NftListDataTransferObject>