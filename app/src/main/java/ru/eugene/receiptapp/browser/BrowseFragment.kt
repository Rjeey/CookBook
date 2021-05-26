package ru.eugene.receiptapp.browser

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.eugene.receiptapp.MyAppIntro
import ru.eugene.receiptapp.R
import ru.eugene.receiptapp.creator.ReceiptCreatorFragment
import ru.eugene.receiptapp.database.DatabaseDataProvider
import ru.eugene.receiptapp.model.IReceipt


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BrowseFragment : Fragment(), SearchView.OnQueryTextListener {
    private val BROWSER_DEBUG_TAG = "BrowseFragment"
    private lateinit var searchView: SearchView
    private lateinit var browserAdapter: BrowserRecycleAdapter
    private var receipts: Array<IReceipt> = emptyArray()
    private lateinit var filteredReceipts: Array<IReceipt>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dataProvider: DatabaseDataProvider
    private var searchQuery: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            if (!sharedPref.getBoolean("hasPassedTutorial", false)) {
                val editor = sharedPref.edit()
                editor.putBoolean("hasPassedTutorial", true)
                editor.apply()
                openIntro()
            }
        }
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // When the view is created, we should
        dataProvider = DatabaseDataProvider(requireContext())
        dataProvider.getAllReceipts(::onFetchReceipts)

        browserAdapter = BrowserRecycleAdapter(receipts)
        browserAdapter.setOnReceiptClick(::onReceiptEdit)
        browserAdapter.setOnReceiptLongClick(::onReceiptDelete)
        val recyclerView: RecyclerView = view.findViewById(R.id.browser_list)
        recyclerView.adapter = browserAdapter
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                linearLayoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        val createButton = view.findViewById<ImageButton>(R.id.fab)
        createButton.setOnClickListener(View.OnClickListener {
            openReceiptEditor()
        })
    }

    private fun onFetchReceipts(arrayOfReceipts: Array<IReceipt>) {
        receipts = arrayOfReceipts
        updateReceiptsList()
    }

    private fun updateReceiptsList() {
        activity?.runOnUiThread(Runnable {
            val searchQuery = this.searchQuery
            filteredReceipts = if (searchQuery != null) {
                filterReceipts(receipts, searchQuery)
            } else {
                receipts
            }
            browserAdapter.updateReceipts(filteredReceipts)
        });
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem: MenuItem = menu.findItem(R.id.search)
        searchView = mSearchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.isIconifiedByDefault = false;
        searchView.isFocusable = true;
        searchView.isIconified = false;
        searchView.requestFocusFromTouch();
        val help: MenuItem = menu.findItem(R.id.action_help)
        help.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item: MenuItem? ->
            openIntro()
            true
        });
        val about: MenuItem = menu.findItem(R.id.action_about)
        about.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener { item: MenuItem? ->
            openAbout()
            true
        });
    }

    fun onReceiptEdit(receipt: IReceipt) {
        openReceiptEditor(receipt)
    }

    fun onReceiptDelete(receipt: IReceipt) {
        dataProvider.deleteReceipt(receipt.id, ::refresh)
    }

    fun filterReceipts(receipts: Array<IReceipt>, searchQuery: String): Array<IReceipt> {
        return receipts.filter { receipt -> isReceiptOk(receipt, searchQuery)}.toTypedArray()
    }

    fun refresh() {
        dataProvider.getAllReceipts(::onFetchReceipts)
    }

    fun isReceiptOk(receipt: IReceipt, searchQueryText: String): Boolean {
        val searchQuery = searchQueryText.toLowerCase()
        if (receipt.body.toLowerCase().contains(searchQuery)) {
            return true
        }
        if (receipt.creationDate.toString().toLowerCase().contains(searchQuery)) {
            return true
        }
        if (receipt.title.toLowerCase().contains(searchQuery)) {
            return true
        }
        if (receipt.type.toString().toLowerCase().contains(searchQuery)) {
            return true
        }
        return false
    }

    fun openReceiptEditor(receipt: IReceipt? = null) {
        activity?.runOnUiThread(Runnable {
            val transaction = parentFragmentManager.beginTransaction()
            val editorFragment = ReceiptCreatorFragment()
            if (receipt != null) {
                editorFragment.setTargetReceipt(receipt)
            }
            transaction.addToBackStack("Emmm")
            transaction.replace(R.id.nav_host_fragment, editorFragment)
            transaction.commit()
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        updateReceiptsList()
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newSearchText: String?): Boolean {
        searchQuery = newSearchText
        updateReceiptsList()
        return true
    }

    private fun openIntro() {
        startActivity(Intent(context, MyAppIntro::class.java))
    }

    private fun openAbout() {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("About the app")
        //set message for alert dialog
        builder.setMessage("This app was made by Rjeey gr. 881074")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> Unit })
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}