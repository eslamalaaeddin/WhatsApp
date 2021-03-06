package com.example.whatsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.R
import com.example.whatsapp.models.ContactsModel
import com.squareup.picasso.Picasso

class AddedContactsAdapterGroupCreationActivity(items: MutableList<ContactsModel>)
    : RecyclerView.Adapter<AddedContactsAdapterGroupCreationActivity.MyViewHolder>() {

    private var list = items


    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val contact = list[holder.adapterPosition]
        holder.bind(contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.added_participant_item_layout,
                parent,
                false
            )
        )
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameTextView : TextView = itemView.findViewById(R.id.user_name_text_view)
        private val userImageView : ImageView = itemView.findViewById(R.id.user_image_view)
        private val removeContactImageView: ImageView = itemView.findViewById(R.id.contact_removed_image_view)



        init {
            itemView.setOnClickListener(this)

        }

        fun bind(contactsModel: ContactsModel) {

            nameTextView.text = list[adapterPosition].name
            if (list[adapterPosition].image.isNotEmpty()) {
                Picasso.get()
                    .load(list[adapterPosition].image)
                    .placeholder(R.drawable.ic_person)
                    .into(userImageView)
            }
            removeContactImageView.visibility = View.GONE
        }

        override fun onClick(itemView: View?) {

        }
    }

}