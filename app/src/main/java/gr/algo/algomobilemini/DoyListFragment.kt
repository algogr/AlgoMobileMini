package gr.algo.algomobilemini


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.fragment_customer.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DoyListFragment : Fragment() {

    private lateinit  var listener:OnItemSelectedListener
    private  var cusstomerFragment=CustomerActivityFragment()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_doy_list, container, false)
        val listview=view.findViewById<ListView>(R.id.listViewDoy)
        val searchview=view.findViewById<SearchView>(R.id.searchViewDoy)
        val handler=MyDBHandler(context=this.context,version = 1,name=null,factory = null)

        val doyList:MutableList<Doy>?=handler.getDoys()

        val doyListAdapter= DoyListViewAdapter(this.context, doyList)


        listview.adapter=doyListAdapter

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText


                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                doyListAdapter.filter(text)
                return false
            }
        })



        return view
    }

    inner class DoyListViewAdapter: BaseAdapter {

        private var doyList:MutableList<Doy>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts: HashMap<String, String>
        private  var tempNameVersionList: ArrayList<Doy>





        constructor(context: Context, doylist: MutableList<Doy>?) : super(){
            this.doyList=doylist
            this.tempNameVersionList = ArrayList(doyList)
            this.context=context
            this.hashMapTexts= HashMap()
            this.mInflator= LayoutInflater.from(context)
            for ( doy in doyList!!) {

                this.hashMapTexts.put(doy.description,"")


            }

        }




        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: ViewHolder
            val view: View


            if (convertView==null){
                view= layoutInflater.inflate(R.layout.doy_item,parent,false)
                holder= ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as ViewHolder
            }


            holder.position=position
            holder.textView.text=doyList!![position].description

            return view
        }

        override fun getItem(position: Int): Any {
            return doyList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return doyList!!.size
        }





        fun filter(text: String?) {


            //Our Search text
            val text = text!!.toLowerCase(Locale.getDefault())

            doyList?.clear()

            //Here We Clear Both ArrayList because We update according to Search query.



            if (text.length == 0) {

                /*If Search query is Empty than we add all temp data into our main ArrayList

                We store Value in temp in Starting of Program.

                */

                doyList?.addAll(tempNameVersionList)


            } else {


                for (i in 0..tempNameVersionList.size - 1) {

                    /*
                    If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                    if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                    */

                    if (tempNameVersionList.get(i).description!!.toLowerCase(Locale.getDefault()).contains(text)) {


                        doyList?.add(tempNameVersionList.get(i))


                    }



                }
            }

            //This is to notify that data change in Adapter and Reflect the changes.
            notifyDataSetChanged()


        }


        inner class ViewHolder(view: View?){
            var textView: TextView
            var position:Int

            init{

                this.textView=view?.findViewById<TextView>(R.id.doyText) as TextView

                this.position=-1
                view.setOnClickListener { v: View ->

                    val test = activity as CustomerActivity
                    test.CoordinatorLayout1.removeAllViews()
                    val fragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
                    val args=Bundle()
                    args.putString("doydescr",doyList!![position].description)
                    args.putInt("doyerpid",doyList!![position].erpId)


                    args.putString("cusname",arguments.getString("cusname"))
                    args.putString("address",arguments.getString("address"))
                    args.putString("district",arguments.getString("district"))
                    args.putString("title",arguments.getString("title"))
                    args.putString("afm",arguments.getString("afm"))
                    args.putString("occupation",arguments.getString("occupation"))
                    args.putString("tel1",arguments.getString("tel1"))
                    args.putString("tel2",arguments.getString("tel2"))
                    args.putString("fax",arguments.getString("fax"))
                    args.putString("email",arguments.getString("email"))
                    args.putInt("vatstatusid",arguments.getInt("vatstatusid"))
                    args.putString("city",arguments.getString("city"))
                    args.putString("comments",arguments.getString("comments"))
                    args.putInt("routeid",arguments.getInt("routeid"))
                    args.putInt("mode",1)

                    cusstomerFragment.arguments=args

                    fragmentTransaction.replace(R.id.CoordinatorLayout1,cusstomerFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                                     }
            }
        }
    }

    fun setOnItemSelectedListener(listener:OnItemSelectedListener)
    {
        this.listener=listener
    }



    interface OnItemSelectedListener{

        fun onItemSelected(position:Int,iteId:Int):Int

    }








}
