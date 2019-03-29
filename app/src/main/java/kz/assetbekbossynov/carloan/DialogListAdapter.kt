package kz.assetbekbossynov.payday

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filterable
import kotlinx.android.synthetic.main.spinner_list_item.view.*

class DialogListAdapter(private val context: Context?, private var list: ArrayList<String>, private val editText: EditText?): RecyclerView.Adapter<DialogListAdapter.ViewHolder>() {

    internal var displayedList: ArrayList<String> = list
    internal var itemId: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.spinner_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return displayedList.size
    }

    fun getItemId(): Int {
        return itemId
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.item.setText(displayedList[position])
        holder.itemView.item.setOnClickListener(View.OnClickListener {
            itemId = position
            editText!!.setText(displayedList[position])
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}