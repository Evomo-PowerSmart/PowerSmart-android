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
            val formattedText = SpannableStringBuilder()
            val anomalyType = notification.anomalyType
            val anomalyTypeSpan = SpannableString(anomalyType).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, anomalyType.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            formattedText.append(anomalyTypeSpan)
            formattedText.append("\n")

            val formattedDate = notification.readingTime.toTimestamp()?.toFormattedString()
            val positionTimeText = "${notification.position}, $formattedDate WIB"
            formattedText.append(positionTimeText)

            tvMessage.text = formattedText
            ivIcon.setImageResource(R.drawable.ic_notification)

            itemView.setOnClickListener {
                onItemClick(notification)
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