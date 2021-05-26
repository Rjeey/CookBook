package ru.eugene.receiptapp.model

import java.util.Date

interface IReceipt {
    val title: String
    val type: ReceiptType
    val creationDate: Date
    val body: String
    val id: Int
}