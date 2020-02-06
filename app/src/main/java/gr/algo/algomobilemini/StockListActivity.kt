package gr.algo.algomobilemini

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_invoice_list.*

import kotlinx.android.synthetic.main.content_store_status.*
import kotlinx.android.synthetic.main.storestatus_item.*
import java.util.*

class StockListActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_status)
        toolbar.setTitle("Κατάσταση Αποθήκης")

        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)


        val storeList:MutableList<Material>? = handler.getAllItems()
        val storelistAdapter=StoreListViewAdapter(this@StockListActivity,storeList)


        listViewStore.adapter=storelistAdapter










    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i= Intent(this,ReportsMenuActivity::class.java)

        startActivity(i)
    }


    inner class StoreListViewAdapter: BaseAdapter {

        private var storeList:MutableList<Material>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts: HashMap<String, String>





        constructor(context: Context, storelist: MutableList<Material>?) : super(){
            this.storeList=storelist
            this.context=context
            this.hashMapTexts= HashMap()
            this.mInflator= LayoutInflater.from(context)
            for ( item in storeList!!) {

                this.hashMapTexts.put(item.description,"")

            }

        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: StockListActivity.ViewHolder
            val view: View


            if (convertView==null){
                view= layoutInflater.inflate(R.layout.storestatus_item,parent,false)
                holder= ViewHolder(view)

                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as ViewHolder
            }


            holder.position=position
            holder.itemText.text=storeList!![position].code+" "+storeList!![position].description
            holder.balanceText.text=storeList!![position].balance.toString()




            return view
        }

        override fun getItem(position: Int): Any {
            return storeList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return storeList!!.size
        }







    }


    inner class ViewHolder(view: View?){

        var itemText:TextView
        var balanceText:TextView
        var position:Int



        init{

            this.itemText=view?.findViewById<TextView>(R.id.itemTextView) as TextView
            this.balanceText=view?.findViewById<TextView>(R.id.balanceTextView) as TextView
            this.position=-1







        }
    }








}
