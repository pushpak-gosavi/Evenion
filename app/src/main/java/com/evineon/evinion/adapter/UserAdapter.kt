package com.evineon.evinion.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.evineon.evinion.R
import com.evineon.evinion.model.User
import com.evineon.evinion.ui.fragment.ListFragmentDirections
import kotlinx.android.synthetic.main.card_row_users_list.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private var userList= emptyList<User>()
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view= LayoutInflater.from(parent.context)
               .inflate(R.layout.card_row_users_list, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem= userList[position]
        holder.itemView.tvFirstAndLastName.text= "${currentItem.firstName} ${currentItem.lastName}"
        holder.itemView.tvDescription.text= currentItem.introduction

        val image: ByteArray = currentItem.image
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        holder.itemView.ivProfile.setImageBitmap(bitmap)

        holder.itemView.rowLayout.setOnClickListener {
            val action= ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(newUserList: List<User>){
        this.userList= newUserList
        notifyDataSetChanged()
    }

}