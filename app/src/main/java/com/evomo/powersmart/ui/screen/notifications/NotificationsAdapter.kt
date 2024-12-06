package com.evomo.powersmart.ui.screen.notifications

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.evomo.powersmart.R
import com.evomo.powersmart.data.anomaly.model.AnomalyResponseItem
import com.evomo.powersmart.ui.screen.home.components.toFormattedString
import com.evomo.powersmart.ui.utils.toTimestamp

class NotificationsAdapter(
    private val onItemClick: (AnomalyResponseItem) -> Unit
) : ListAdapter<AnomalyResponseItem, NotificationsAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvMessage: TextView = itemView.findViewById(R.id.tv_message)

        fun bind(notification: AnomalyResponseItem) {
            // Membuat SpannableStringBuilder untuk format teks
            val formattedText = SpannableStringBuilder()

            // Menambahkan anomalyType dengan gaya bold
            val anomalyType = notification.anomalyType
            val anomalyTypeSpan = SpannableString(anomalyType).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, anomalyType.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Menambahkan anomalyType ke formattedText
            formattedText.append(anomalyTypeSpan)

            // Menambahkan \n untuk pemisah
            formattedText.append("\n")

            // Mengubah readingTime menjadi Timestamp dan memformatnya
            val formattedDate = notification.readingTime.toTimestamp()?.toFormattedString()

            // Menambahkan position dan formattedDate
            val positionTimeText = "${notification.position}, $formattedDate"
            formattedText.append(positionTimeText)

            // Menetapkan formattedText ke TextView
            tvMessage.text = formattedText

            // Set the icon (you can use different icons based on the notification type)
            ivIcon.setImageResource(R.drawable.ic_notification) // Ganti dengan resource ikon yang sesuai

            // Mengatur OnClickListener untuk item view
            itemView.setOnClickListener {
                onItemClick(notification) // Kirim data anomaly yang diklik
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AnomalyResponseItem>() {
            override fun areItemsTheSame(oldItem: AnomalyResponseItem, newItem: AnomalyResponseItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AnomalyResponseItem, newItem: AnomalyResponseItem) =
                oldItem == newItem
        }
    }
}

