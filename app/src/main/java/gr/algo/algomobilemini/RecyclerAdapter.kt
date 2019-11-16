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

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val titles = arrayOf("Τιμολόγηση",
            "Ταμείο", "Αναφορές", "Εργαλεία Διαχείρισης",
            "Επικοινωνία με ERP")

    private val details = arrayOf("Έκδοση Τιμολογίων", "Συναλλαγές με μετρητά",
            "Καταστάσεις αποθήκης/πωλήσεων/εισπράξεων", "Ρυθμίσεις συστήματος",
            "Upload/Download βάσης δεομένων")

    private val images = intArrayOf(R.drawable.mm_image_1,
            R.drawable.mm_image_2, R.drawable.mm_image_3,
            R.drawable.mm_image_4, R.drawable.mm_image_5)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.item_title
            itemDetail = itemView.item_detail

            itemView.setOnClickListener { v: View  ->
                var position: Int = getAdapterPosition()
                when(position){
                    0->{

                        val i = Intent(v.context,RouteActivity::class.java)
                        v.context.startActivity(i)

                    }
                    1->{
                        val i = Intent(v.context,CashMenuActivity::class.java)
                        v.context.startActivity(i)
                    }
                    2->{
                        val i = Intent(v.context,InvoiceListActivity::class.java)
                        v.context.startActivity(i)
                    }

                    3->{
                        //val i = Intent(v.context,Settings::class.java)
                        //v.context.startActivity(i)
                        val bt=BTPrint()
                        bt.findCustomBt()
                        //bt.openCustomBT()

                        //bt.invoicePlainData()
                        bt.closeBT()
                        //sendData()
                        // closeBT()
                    }

                    4->{
                        val i = Intent(v.context,UploadDB::class.java)
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



