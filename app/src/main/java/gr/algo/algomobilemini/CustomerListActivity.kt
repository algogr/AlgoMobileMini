package gr.algo.algomobilemini

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_customer_list.*
import java.util.*
import kotlin.collections.ArrayList

class CustomerListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)
        setTitle("Πελάτες Δρομολογίου")
        val listview = findViewById<ListView>(R.id.listView)

        //Find View By Id For SearchView
        val searchView = findViewById<SearchView>(R.id.searchView) as SearchView
        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)

        val extras=intent.extras?:return

        val routeId=extras.getInt("routeid")

        val customerList:MutableList<Customer>? = handler.getCustomersByRoute(routeId)
        val customerlistAdapter=CustomerListViewAdapter(this@CustomerListActivity,customerList)


        listview.adapter=customerlistAdapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText


                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                customerlistAdapter.filter(text)
                return false
            }
        })


        fab.setOnClickListener { view ->
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //     .setAction("Action", null).show()

            val i= Intent(view.context,CustomerActivity::class.java)
            i.putExtra("routeid",routeId)
            startActivity(i)

        }
    }







    inner class CustomerListViewAdapter: BaseAdapter {

        private var CustomerList:MutableList<Customer>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts:  HashMap<String,String>
        private  var tempNameVersionList:ArrayList<Customer>




        constructor(context: Context, customerlist: MutableList<Customer>?) : super(){
            this.CustomerList=customerlist
            this.tempNameVersionList = ArrayList(CustomerList)
            this.context=context
           this.hashMapTexts= HashMap()
           this.mInflator= LayoutInflater.from(context)
           for ( customer in CustomerList!!) {

                this.hashMapTexts.put(customer.name,"")

            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: CustomerListActivity.ViewHolder
            val view: View


            if (convertView==null){
                view= layoutInflater.inflate(R.layout.customerlist_item,parent,false)
                holder= CustomerListActivity.ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as CustomerListActivity.ViewHolder
            }


            holder.position=position
            holder.textView.text=CustomerList!![position].name
            holder.cusId=CustomerList!![position].erpid
            holder.name=CustomerList!![position].name
            holder.title=CustomerList!![position].title
            holder.address=CustomerList!![position].address
            holder.city=CustomerList!![position].city
            holder.vatStatusId=CustomerList!![position].vatstatusid


            return view
        }

        override fun getItem(position: Int): Any {
            return CustomerList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return CustomerList!!.size
        }





        fun filter(text: String?) {


            //Our Search text
            val text = text!!.toLowerCase(Locale.getDefault())

            CustomerList?.clear()

            //Here We Clear Both ArrayList because We update according to Search query.



            if (text.length == 0) {

                /*If Search query is Empty than we add all temp data into our main ArrayList

                We store Value in temp in Starting of Program.

                */

                CustomerList?.addAll(tempNameVersionList)


            } else {


                for (i in 0..tempNameVersionList.size - 1) {

                    /*
                    If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                    if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                    */

                    if (tempNameVersionList.get(i).name!!.toLowerCase(Locale.getDefault()).contains(text)) {


                        CustomerList?.add(tempNameVersionList.get(i))


                    }



                }
            }

            //This is to notify that data change in Adapter and Reflect the changes.
            notifyDataSetChanged()


        }


    }


    private class ViewHolder(view: View?){
        var textView: TextView
        var position:Int
        var cusId:Int
        var title:String
        var address:String
        var city:String
        var name:String
        var vatStatusId:Int

        init{

            this.textView=view?.findViewById<TextView>(R.id.textView3) as TextView

            this.position=-1
            this.cusId=-1
            this.title=""
            this.address=""
            this.name=""
            this.city=""
            this.vatStatusId=-1

            view.setOnClickListener { v: View  ->
                /*
                var position: Int = getAdapterPosition()
                when(position){
                    0->{

                        val i = Intent(v.context,RouteActivity::class.java)
                        v.context.startActivity(i)

                    }
                }
                */


                val i = Intent(v.context,InvoiceHeaderActivity::class.java)
                i.putExtra("cusid",cusId)
                i.putExtra("name",name)
                i.putExtra("title",title)
                i.putExtra("address",address)
                i.putExtra("city",city)
                i.putExtra("vatstatusid",vatStatusId)



                v.context.startActivity(i)
            }
        }
    }








}