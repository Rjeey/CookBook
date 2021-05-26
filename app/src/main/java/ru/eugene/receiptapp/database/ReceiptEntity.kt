package ru.eugene.receiptapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.eugene.receiptapp.model.IReceipt
import ru.eugene.receiptapp.model.ReceiptType
import java.util.*

@Entity(tableName = "receipt")
data class ReceiptEntity (
        @ColumnInfo(name = "title") val dbTitle: String,
        @ColumnInfo(name = "type") val dbType: String,
        @ColumnInfo(name = "content") val dbContent: String,
        @ColumnInfo(name = "creationDate") val dbCreateDate: Date,
        @PrimaryKey(autoGenerate = true) val uid: Int = 0
) : IReceipt {
    override val title: String
        get() = dbTitle
    override val type: ReceiptType
        get() = ReceiptType.valueOf(dbType)
    override val creationDate: Date
        get() = dbCreateDate
    override val body: String
        get() = dbContent
    override val id: Int
        get() = uid
}
