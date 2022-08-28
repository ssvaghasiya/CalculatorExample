package com.task.flybytsassign.ui.adapter

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.task.flybytsassign.R
import com.task.flybytsassign.ui.model.LastOperationData

class CustomAdapter(private val mList: List<LastOperationData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_data, parent, false)

        return ViewHolder(view)
    }

    private fun getItems(position: Int): LastOperationData {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lastOperationData = getItems(position)
        lastOperationData.apply {
            holder.textViewExpression.text = userInput
            holder.textViewResult.text = result
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewExpression: TextView = itemView.findViewById(R.id.textViewExpression)
        val textViewResult: TextView = itemView.findViewById(R.id.textViewResult)
    }
}