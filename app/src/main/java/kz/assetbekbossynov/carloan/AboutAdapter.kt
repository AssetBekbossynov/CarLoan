package kz.assetbekbossynov.payday

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_view_item.view.*
import java.util.ArrayList

class AboutAdapter(val list: ArrayList<String>, val context: Context): RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AboutAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_item, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: AboutAdapter.ViewHolder, p1: Int) {
        when(list.get(p1)){
            context.getString(R.string.what_is_pd) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.whatis))
                p0.itemView.desc.text = context.getString(R.string.what_is_pd_text)
                p0.itemView.itemTitle.text = context.getString(R.string.what_is_pd)
            }
            context.getString(R.string.put_fast) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.fast))
                p0.itemView.desc.text = context.getString(R.string.put_fast_text)
                p0.itemView.itemTitle.text = context.getString(R.string.put_fast)
            }
            context.getString(R.string.how_they_work) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.howitworks))
                p0.itemView.desc.text = context.getString(R.string.how_they_work_text)
                p0.itemView.itemTitle.text = context.getString(R.string.how_they_work)
            }
            context.getString(R.string.unsecured) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.personalloan))
                p0.itemView.desc.text = context.getString(R.string.unsecured_text)
                p0.itemView.itemTitle.text = context.getString(R.string.unsecured)
            }
            context.getString(R.string.different) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.different))
                p0.itemView.desc.text = context.getString(R.string.different_text)
                p0.itemView.itemTitle.text = context.getString(R.string.different)
            }
            context.getString(R.string.good_idea) ->{
                p0.itemView.iv.setImageDrawable(context.resources.getDrawable(R.drawable.goodidea))
                p0.itemView.desc.text = context.getString(R.string.good_idea_text)
                p0.itemView.itemTitle.text = context.getString(R.string.good_idea)
            }
        }
        val intent = Intent(context, AboutActivity::class.java)
        intent.putExtra("title", p0.itemView.itemTitle.text)
        intent.putExtra("content", p0.itemView.desc.text)
        p0.itemView.setOnClickListener {
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}