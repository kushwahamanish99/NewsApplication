package com.example.educationnews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ViewHolder(v: View){
    val tvTitle: TextView = v.findViewById(R.id.tvTitle)
    val tvDate: TextView = v.findViewById(R.id.tvDate)
    val tvDesc: TextView = v.findViewById(R.id.tvDesc)
    val tvLink: TextView = v.findViewById(R.id.tvLink)
    val tvNumber: TextView = v.findViewById(R.id.tvNumber)
}

class FeedAdaptor(context: Context, private val resource: Int, private val lst: ArrayList<Feed>) : ArrayAdapter<Feed>(context, resource){
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return lst.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if(convertView == null){
            view = inflater.inflate(resource, parent, false)
            viewHolder= ViewHolder(view)
            view.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentObject = lst[position]
        viewHolder.tvDate.text = currentObject.pubdate
        viewHolder.tvDesc.text = currentObject.description
        viewHolder.tvLink.text = currentObject.link
        viewHolder.tvNumber.text = (position+1).toString()
        viewHolder.tvNumber.append(".")
        viewHolder.tvTitle.text = currentObject.title

        return  view
    }
}