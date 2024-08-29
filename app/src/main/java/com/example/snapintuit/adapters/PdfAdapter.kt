package com.example.snapintuit.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.snapintuit.databinding.LayoutPdfCardviewBinding
import com.example.snapintuit.models.PdfData
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class PdfAdapter(private val pdfList: List<PdfData>) :
    RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {
    lateinit var customListener: CustomListener

    inner class PdfViewHolder(val binding: LayoutPdfCardviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val binding =
            LayoutPdfCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdfInfo = pdfList[position]
        holder.binding.pdfTitle.text = pdfInfo.title
        holder.binding.pdfSize.text = pdfInfo.size
        holder.binding.pdfCreationDate.text = formatTimestamp(pdfInfo.creationDate)
        holder.binding.pdfCard.setOnClickListener {
            customListener.onItemClicked(pdfInfo)
        }


    }
    override fun getItemCount(): Int = pdfList.size

    interface CustomListener {
        fun onItemClicked(pdfData: PdfData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestamp(timestamp: String?): String {
        // Define the input format (ISO 8601)
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        // Parse the input timestamp
        val dateTime = OffsetDateTime.parse(timestamp, inputFormatter)

        // Define the output format
        val outputFormatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm", Locale.getDefault())

        // Convert to the desired time zone (optional)
        val localDateTime =
            dateTime.withOffsetSameInstant(ZoneId.systemDefault().rules.getOffset(dateTime.toInstant()))
                .toLocalDateTime()

        // Format the LocalDateTime
        return localDateTime.format(outputFormatter)
    }

}
