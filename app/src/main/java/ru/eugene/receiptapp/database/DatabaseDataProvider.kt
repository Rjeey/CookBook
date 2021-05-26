package ru.eugene.receiptapp.database

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.eugene.receiptapp.model.IReceipt
import ru.eugene.receiptapp.model.ReceiptType
import java.util.*

class DatabaseDataProvider (context: Context) {
    private val databaseInstance = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "receipt.db"
    ).build()

    fun getAllReceipts(onComplete: (Array<IReceipt>) -> Unit) {
        GlobalScope.launch {
            onComplete(databaseInstance.receiptDao().getAll().toTypedArray())
        }
    }

    fun createReceipt(title: String, body: String, type: ReceiptType, creationDate: Date, onComplete: () -> Unit) {
        GlobalScope.launch {
            val newReceipt = ReceiptEntity(title, type.toString(), body, creationDate)
            databaseInstance.receiptDao().insert(newReceipt)
            onComplete()
        }
    }

    fun updateReceipt(id: Int, title: String, body: String, type: ReceiptType, creationDate: Date, onComplete: () -> Unit) {
        GlobalScope.launch {
            val receiptToUpdate = ReceiptEntity(title, type.toString(), body, creationDate, id)
            databaseInstance.receiptDao().update(receiptToUpdate)
            onComplete()
        }
    }

    fun deleteReceipt(id: Int, onComplete: () -> Unit) {
        GlobalScope.launch {
            databaseInstance.receiptDao().delete(id)
            onComplete()
        }
    }
}