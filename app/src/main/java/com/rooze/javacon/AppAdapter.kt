package com.rooze.javacon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rooze.javacon.room.AccountEntity

class AppAdapter : Adapter<AppAdapter.AppViewHolder>() {

    private val list: MutableList<AccountEntity> = ArrayList()

    fun updateList(list: List<AccountEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_account, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.updateUsername(list[position].username)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AppViewHolder(view: View) : ViewHolder(view) {
        private val username: TextView = view.findViewById(R.id.username)

        fun updateUsername(text: String) {
            username.text = text
        }
    }
}