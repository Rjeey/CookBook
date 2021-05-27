package ru.eugene.receiptapp.browser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.eugene.receiptapp.R
import ru.eugene.receiptapp.model.IReceipt
import ru.eugene.receiptapp.model.ReceiptType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class BrowserRecycleAdapter (private var receipts: Array<IReceipt>) :
    RecyclerView.Adapter<BrowserRecycleAdapter.ViewHolder>(),
        View.OnClickListener,
        View.OnLongClickListener {
    private var recyclerView: RecyclerView? = null
    private var onItemClickListener: ((IReceipt) -> Unit)? = null
    private var onItemLongClickListener: ((IReceipt) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val view: View = view
        private val titleTextView: TextView = view.findViewById(R.id.receipt_title)
        private val bodyTextView: TextView = view.findViewById(R.id.receipt_body_text)
        private val dateTextView: TextView = view.findViewById(R.id.receipt_date_text)
        private val typeTextView: TextView = view.findViewById(R.id.receipt_type_text)

        fun setTitle(title: String) {
            titleTextView.text = title;
        }

        fun setBody(bodyText: String) {
            bodyTextView.text = bodyText;
        }

        @SuppressLint("SimpleDateFormat")
        fun setCreationDate(creationDate: Date) {
            val dateFormat: DateFormat = SimpleDateFormat("dd-mm-yyyy")
            dateTextView.text = dateFormat.format(creationDate)
        }

        fun setType(type: ReceiptType) {
            typeTextView.text = type.toString();
        }

        fun fromReceipt(receipt: IReceipt) {
            setTitle(receipt.title)
            setCreationDate(receipt.creationDate)
            setType(receipt.type)
            setBody(receipt.body)
        }

        fun setOnClickListener(onClick: View.OnClickListener?) {
            view.setOnClickListener(onClick)
        }

        fun setOnLongClickListener(onClick: View.OnLongClickListener?) {
            view.setOnLongClickListener(onClick)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = receipts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fromReceipt(receipts[position])
        holder.setOnClickListener(this)
        holder.setOnLongClickListener(this)
    }

    fun setOnReceiptClick(callback: (IReceipt) -> Unit) {
        onItemClickListener = callback
    }

    fun setOnReceiptLongClick(callback: (IReceipt) -> Unit) {
        onItemLongClickListener = callback
    }

    fun updateReceipts(newReceipts: Array<IReceipt>) {
        receipts = newReceipts
        notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val index = recyclerView?.getChildLayoutPosition(v);
            if (index != null) {
                onItemClickListener?.invoke(receipts[index.toInt()])
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v != null) {
            val index = recyclerView?.getChildLayoutPosition(v);
            if (index != null) {
                onItemLongClickListener?.invoke(receipts[index.toInt()])
                return true
            }
        }
        return false
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

}