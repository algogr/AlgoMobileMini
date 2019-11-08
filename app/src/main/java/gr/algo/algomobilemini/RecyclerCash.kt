package gr.algo.algomobilemini





import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerCash : RecyclerView.Adapter<RecyclerCash.ViewHolder>() {

    private val titles = arrayOf("Εισπράξεις",
            "Εξοδα", "'Ανοιγμα")

    private val details = arrayOf("Εισπράξεις πελατών", "Διάφορα έξοδα",
            "Άνοιγμα ταμείου")

    private val images = intArrayOf(R.drawable.collections,
             R.drawable.expenses, R.drawable.cashier)

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

                        val i = Intent(v.context, CustomerListActivity::class.java)
                        i.putExtra("mode", 1)
                        v.context.startActivity(i)

                    }
                    1 -> {
                        val i = Intent(v.context, ExpensesActivity::class.java)
                        v.context.startActivity(i)

                    }
                    2 -> {
                        val i = Intent(v.context, CashOpenActivity::class.java)
                        v.context.startActivity(i)

                    }


                }


                //Snackbar.make(v, "Click detected on item $position",
                //      Snackbar.LENGTH_LONG).setAction("Action", null).show()
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



