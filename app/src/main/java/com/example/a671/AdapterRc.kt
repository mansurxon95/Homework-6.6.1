package com.example.a671

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a671.databinding.ItemViewBinding

class AdapterRc (var onClik: OnClik): ListAdapter<Sound, AdapterRc.VH>(MyDiffUtill()) {

    inner class VH(var itemview : ItemViewBinding): RecyclerView.ViewHolder(itemview.root){

        fun onBind(user: Sound,position: Int){

            itemview.itemImage.setImageURI(user.image!!)
            itemview.itemName.text = user.artist
            itemview.itemMp3Name.text = user.title




            itemview.viewBtn.setOnClickListener {
                onClik.view(user, position)
            }



        }

    }

    class MyDiffUtill: DiffUtil.ItemCallback<Sound>(){
        override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.onBind(item,position)
    }

    interface OnClik{

        fun view(contact: Sound,position: Int){

        }
    }


}