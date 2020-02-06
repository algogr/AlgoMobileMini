package gr.algo.algomobilemini

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerReports: RecyclerView.Adapter<RecyclerReports.ViewHolder>() {
    private val titles = arrayOf("Παραστατικά",
            "Αποθήκη")

    private val details = arrayOf("Κατάσταση παραστατικών", "Κατάσταση υπολοίπων ειδών")

    private val images = intArrayOf(R.drawable.doclist,
            R.drawable.stock)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.item_title
            itemDetail = itemView.item_detail

            itemView.setOnClickListener { v: View ->
                var position: Int = getAdapterPosition()
                when (position) {
                    0 -> {

                        val i = Intent(v.context, InvoiceListActivity::class.java)
                        i.putExtra("mode", 1)
                        v.context.startActivity(i)

                    }
                    1 -> {
                        val i = Intent(v.context, StockListActivity::class.java)
                        v.context.startActivity(i)

                    }


                }



            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemImage.setImageResource(images[i])
    }

    override fun getItemCount(): Int {
        return titles.size
    }

}