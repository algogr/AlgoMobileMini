package gr.algo.algomobilemini

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_route.*
import  gr.algo.algomobilemini.MyDBHandler


class RouteActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_route)
        setTitle("Δρομολόγια")
        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)

        val routeList:MutableList<Route>? = handler.getAllRoutes()

        val routeListAdapter=RouteListViewAdapter(this@RouteActivity,routeList)



        listView.adapter=routeListAdapter







    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i= Intent(this,MainActivity::class.java)

        startActivity(i)
    }



    inner class RouteListViewAdapter:BaseAdapter{

        private var routeList:MutableList<Route>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts:  HashMap<String,String>



        constructor(context: Context,routelist: MutableList<Route>?) : super(){
            this.routeList=routelist
            this.context=context
            this.hashMapTexts= HashMap()
            this.mInflator= LayoutInflater.from(context)
            for ( route in routeList!!) {

                this.hashMapTexts.put(route.description,"")

            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: RouteActivity.ViewHolder
            val view: View

            if (convertView==null){
                view= layoutInflater.inflate(R.layout.route_item,parent,false)
                holder=ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as ViewHolder
            }


            holder.position=position
            holder.textView.text=routeList!![position].description
            holder.routeId=routeList!![position].erpId
            Log.d("JIM-ROUTEID0","v")
            return view
        }

        override fun getItem(position: Int): Any {
            return routeList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return routeList!!.size
        }


    }


    private class ViewHolder(view: View?){
        var textView: TextView
        var position:Int
        var routeId=-1

        init {
            this.textView = view?.findViewById<TextView>(R.id.textView) as TextView
            this.position = -1

            view.setOnClickListener { v: View ->



                val i = Intent(v.context, CustomerListActivity::class.java)

                i.putExtra("routeid", routeId)
                v.context.startActivity(i)
            }

        }

    }



}
