package com.benmohammad.newshakz.data.room

import com.benmohammad.newshakz.Mockable
import com.benmohammad.newshakz.data.model.Item
import com.benmohammad.newshakz.data.model.RoomItem
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

@Mockable
class ItemStore(
    private val dataBase: ItemDatabase,
    private val itemDao: ItemDao = dataBase.itemDao()
) {

    fun insertItems(items: List<Item>) :Completable {
        return Completable.fromCallable{
            val roomItems = items.map {
                RoomEntityMapper.itemToRoomItem(
                    it
                )
            }
            itemDao.insertItems(roomItems)
        }
            .subscribeOn(Schedulers.io())

    }

    object RoomEntityMapper {
        fun itemToRoomItem(item: Item) =
            RoomItem(
                item.id,
                item.isDeleted,
                item.type,
                item.authorName,
                item.publishDate,
                item.isDead,
                item.parent,
                item.text,
                item.url,
                item.title,
                item.score,
                item.descendants
            )

    }

}