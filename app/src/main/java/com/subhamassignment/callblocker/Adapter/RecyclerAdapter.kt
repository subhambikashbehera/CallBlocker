package com.subhamassignment.callblocker.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.R
import com.subhamassignment.callblocker.UpdateNumber
import com.subhamassignment.callblocker.dataBaseHandle.NumberDatabase
import com.subhamassignment.callblocker.databinding.NumberRowItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.myviewholder> {

    var context: Context
    var numberlist = arrayListOf<NumberModel_table>()

    constructor(context: Context, numberlist: ArrayList<NumberModel_table>) : super() {
        this.context = context
        this.numberlist = numberlist
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {

        val binding: NumberRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.number_row_item,
            parent,
            false
        )
        return myviewholder(binding)

    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {
        var numdata = numberlist[position]
        holder.bind(numdata)

        holder.itemView.setOnClickListener {

            val intent=Intent(context,UpdateNumber::class.java)
            intent.putExtra("id",numdata.id)
            intent.putExtra("name",numdata.name)
            intent.putExtra("number",numdata.number)
            intent.putExtra("tc",numdata.calltime)
            intent.putExtra("nc",numdata.timescalled)
            context.startActivity(intent)

        }

        holder.binding.EditButton.setOnClickListener {
            val popup=PopupMenu(context,holder.binding.EditButton)
            popup.inflate(R.menu.menu_option)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_id -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val dm = NumberDatabase.getdbinstance(context)
                            numdata.id?.let { it1 -> dm.Daocall().deletenumber(it1) }
                        }
                        true
                    }
                    R.id.update_id -> {
                        val intent=Intent(context,UpdateNumber::class.java)
                        intent.putExtra("id",numdata.id)
                        intent.putExtra("name",numdata.name)
                        intent.putExtra("number",numdata.number)
                        intent.putExtra("tc",numdata.calltime)
                        intent.putExtra("nc",numdata.timescalled)
                        context.startActivity(intent)
                        true
                    }
                    else -> true

                }

            }
            popup.show()




        }



    }

    override fun getItemCount(): Int {
        return numberlist.size
    }

    class myviewholder(var binding: NumberRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(number: NumberModel_table) {
            binding.numberdata = number
            binding.executePendingBindings()
        }




    }


}