package ru.eugene.receiptapp.creator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import ru.eugene.receiptapp.MyAppIntro
import ru.eugene.receiptapp.R
import ru.eugene.receiptapp.database.DatabaseDataProvider
import ru.eugene.receiptapp.model.IReceipt
import ru.eugene.receiptapp.model.ReceiptType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ReceiptCreatorFragment : Fragment() {
    private lateinit var titleTextEditor: EditText
    private lateinit var bodyTextEditor: EditText
    private lateinit var typeSelector: Spinner
    private lateinit var dataProvider: DatabaseDataProvider

    private var receiptToEdit: IReceipt? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_receipt_creator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataProvider = DatabaseDataProvider(requireContext())
        val saveButton = view.findViewById<Button>(R.id.save_button)
        titleTextEditor = view.findViewById(R.id.receipt_name)
        bodyTextEditor = view.findViewById(R.id.receipt_body)
        typeSelector = view.findViewById(R.id.group_spinner)
        if (receiptToEdit != null) {

            titleTextEditor.setText(receiptToEdit?.title)
            bodyTextEditor.setText(receiptToEdit?.body)
            // Oh no, it's retarded
            typeSelector.setSelection(ReceiptType.toInt(receiptToEdit?.type!!))
        }

        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.receipt_type,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            typeSelector.adapter = adapter
        }

        saveButton.setOnClickListener {
            this.onSave()
        }
    }

    fun setTargetReceipt(receipt: IReceipt) {
        receiptToEdit = receipt
    }

    private fun onSave() {
        val receiptType = ReceiptType.fromInt(typeSelector.selectedItemId.toInt())
        val now = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd-mm-yyyy hh:mm:ss")
        val title = if (titleTextEditor.text.isEmpty()) dateFormat.format(now) else titleTextEditor.text.toString()
        val body = bodyTextEditor.text.toString()
        val receiptToEdit = this.receiptToEdit
        if (receiptToEdit != null) {
            dataProvider.updateReceipt(receiptToEdit.id, title, body, receiptType, receiptToEdit.creationDate, ::onSaveSuccess)
        }
        else {
            dataProvider.createReceipt(title, body, receiptType, now, ::onSaveSuccess)
        }
    }

    private fun onSaveSuccess() {
        activity?.runOnUiThread(Runnable {
            parentFragmentManager.popBackStackImmediate()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}