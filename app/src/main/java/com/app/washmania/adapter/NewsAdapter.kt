package com.app.washmania.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.app.washmania.R
import com.app.washmania.model.Category
import com.squareup.picasso.Picasso

class NewsAdapter(
    private val context: Context,
    private val newsList: List<Category.CategoryDatum>,
    private val onNewsSelected: onRowItemSelected
) : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    lateinit var rowView: View

    interface onRowItemSelected {
        fun getPosition(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.row_newsitem, parent, false)

        rowView = itemView
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!newsList[position].categoryPhoto.isNullOrBlank()) {
            Picasso.get()
                .load(newsList[position].categoryPhoto)
                .resize(1024, 628)
                .onlyScaleDown()
                .into(holder.iv_catimage)
        }

        holder.tv_categoryname.text = newsList[position].categoryName


        holder.card_view.setOnClickListener {
            onNewsSelected.getPosition(newsList[position].categoryId)
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_categoryname: TextView
        var iv_catimage: ImageView
        var progressbar: ProgressBar
        var card_view: CardView

        init {
            tv_categoryname = itemView.findViewById(R.id.tv_categoryname)
            iv_catimage = itemView.findViewById(R.id.iv_catimage)
            progressbar = itemView.findViewById(R.id.progressbar)
            card_view = itemView.findViewById(R.id.card_view)
        }
    }

}