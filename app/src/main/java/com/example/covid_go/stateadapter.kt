package com.example.covid_go

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_list.view.*

class stateadapter(val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_list,parent,false)
        val item = list[position]
        view.confirmedTv.text = spannabledata("${item.confirmed}\n↑ ${item.deltaconfirmed ?: 0}", "#bb2205", item.confirmed?.length?:0)
        view.activeTv.text = spannabledata("${item.active}\n↑ ${item.deltaconfirmed ?: 0}", "#0f3057", item.active?.length?:0)
        view.recoveredTv.text = spannabledata("${item.recovered}\n↑ ${item.deltarecovered ?: 0}", "#335d2d", item.recovered?.length?:0)
        view.deathTv.text = spannabledata("${item.deaths}\n↑ ${item.deltadeaths ?: 0}", "#f6830f", item.deaths?.length?:0)
        view.stateTv.text = item.state
        return view

    }

}