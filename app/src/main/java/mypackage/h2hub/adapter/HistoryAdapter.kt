package mypackage.h2hub.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mypackage.h2hub.R
import mypackage.h2hub.databinding.DonationsRowLayoutBinding
import mypackage.h2hub.fragments.donations.DonationsFragmentDirections
import mypackage.h2hub.model.Donation

class HistoryAdapter : ListAdapter<Donation, HistoryAdapter.DonationsViewHolder>(COMPARATOR) {
    class DonationsViewHolder(private var binding: DonationsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(donation: Donation?) {
            binding.donatedFoodItem.text = donation?.foodItem
            binding.donatedFoodItemDescription.text = donation?.description
            if (donation?.photoUri != null && ( donation?.photoUri != "FAKE" || donation?.photoUri != "" )){
                Glide.with(binding.root)
                    .load(donation?.photoUri)
                    .into(binding.foodPic)

            }
            else{
                binding.foodPic.visibility = View.GONE
            }
            binding.donorPhoneNumber.setOnClickListener {
                //start phone call
                val phoneNumber = donation?.phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                binding.root.context.startActivity(intent)
            }

            binding.donorLocation.setOnClickListener {
                val latitude: Float = donation?.location?.latitude?.toFloat() ?: 0.0F
                val longitude: Float = donation?.location?.longitude?.toFloat() ?: 0.0F
                val action =
                    DonationsFragmentDirections.actionDonationsFragmentToDonorLocationFragment(
                        latitude,
                        longitude
                    )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DonationsViewHolder {
        return DonationsViewHolder(
            DonationsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DonationsViewHolder, position: Int) {
        val item = getItem(position)
        val phoneIcon = holder.itemView.findViewById<ImageView>(R.id.donorPhoneNumber)
        val locIcon = holder.itemView.findViewById<ImageView>(R.id.donorLocation)
        phoneIcon.isVisible = false
        locIcon.isVisible = false

        if (item.received == true) {
            val tv = holder.itemView.findViewById<TextView>(R.id.receiveTextView)
            tv.text = "Received"
        } else {
            val tv = holder.itemView.findViewById<TextView>(R.id.receiveTextView)
            tv.text = "Donated"
        }
        holder.bind(item)
    }

    object COMPARATOR : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }
}
