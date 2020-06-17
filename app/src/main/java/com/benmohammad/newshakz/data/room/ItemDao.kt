package com.benmohammad.newshakz.data.room

import androidx.room.*
import com.benmohammad.newshakz.data.model.RoomItem
import io.reactivex.Flowable

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(roomItem: RoomItem): Long

    @Update
    fun updateItem(item: RoomItem)

    @Query("SELECT * FROM item WHERE id = :id")
    fun findItemById(id: Int): RoomItem

    @Query("SELECT * FROM item WHERE parent = :parentId")
    fun findKidsByParent(parentId: Int): Flowable<List<RoomItem>>

    @Query("SELECT * FROM item WHERE favorite = 1")
    fun findFavorites(): Flowable<List<RoomItem>>

    @Query("SELECT * FROM item WHERE viewed = 1")
    fun findViewHistory(): Flowable<List<RoomItem>>

    @Transaction
    fun insertItems(items: List<RoomItem>) {
        items.forEach{upsertItem(it)}
    }

    private fun upsertItem(item: RoomItem) {
        val id = insertItem(item).toInt()
        if(id == -1) {
            updateItem(item)
        }

    }}