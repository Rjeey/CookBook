package ru.eugene.receiptapp.model

enum class ReceiptType {
    BREAKFAST,
    LUNCH,
    DINNER,
    DESSERT;



    companion object {
        fun fromInt(index: Int): ReceiptType {
            when (index) {
                0 -> return BREAKFAST
                1 -> return LUNCH
                2 -> return DINNER
                3 -> return DESSERT
                else -> {
                    // I guess this shouldn't happen
                }
            }

            return BREAKFAST
        }

        fun toInt(type: ReceiptType): Int {
            when (type) {
                BREAKFAST -> return 0
                LUNCH -> return 1
                DINNER -> return 2
                DESSERT -> return 3
            }

            return 0
        }
    }
}