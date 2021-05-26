package ru.eugene.receiptapp.database

import androidx.room.*
import java.util.*

@Dao
interface ReceiptDao {

    @Query("DELETE FROM receipt WHERE uid = :id")
    fun delete(id: Int)

    @Insert
    fun insert(receipt: ReceiptEntity)

    @Update
    fun update(receipt: ReceiptEntity)

    @Query("SELECT * FROM receipt WHERE uid = :uid")
    fun getById(uid: Int): ReceiptEntity

    @Query("SELECT * FROM receipt WHERE creationDate BETWEEN :from AND :to")
    fun getByDate(from: Date, to: Date): ReceiptEntity

    @Query("SELECT * FROM receipt")
    fun getAll(): List<ReceiptEntity>
}