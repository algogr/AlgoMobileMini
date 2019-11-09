package gr.algo.algomobilemini


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_tab1.*
import kotlinx.android.synthetic.main.fragment_tab1_item.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Tab1Fragment : Fragment() {


    private lateinit  var listener:OnItemSelectedListener
    private  var itemFragment=lineItem()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_tab1, container, false)
        val listview=view.findViewById<ListView>(R.id.fullProductListView)
        val searchview=view.findViewById<SearchView>(R.id.itemSearchView)
        val handler=MyDBHandler(context=this.context,version = 1,name=null,factory = null)

        val materialList:MutableList<Material>?=handler.getAllItems()

        val materialListAdapter= MaterialListViewAdapter(this.context, materialList)


        listview.adapter=materialListAdapter

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText


                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                materialListAdapter.filter(text)
                return false
            }
        })



        return view
    }




    inner class MaterialListViewAdapter: BaseAdapter {

        private var MaterialList:MutableList<Material>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts: HashMap<String, String>
        private  var tempNameVersionList: ArrayList<Material>





        constructor(context: Context, materiallist: MutableList<Material>?) : super(){
            this.MaterialList=materiallist
            this.tempNameVersionList = ArrayList(MaterialList)
            this.context=context
            this.hashMapTexts= HashMap()
            this.mInflator= LayoutInflater.from(context)
            for ( material in MaterialList!!) {

                this.hashMapTexts.put(material.description,"")


            }

        }




        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: ViewHolder
            val view: View


            if (convertView==null){
                view= layoutInflater.inflate(R.layout.fragment_tab1_item,parent,false)
                holder= ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as ViewHolder
            }


            holder.position=position
            holder.descrTextView.text=MaterialList!![position].description
            holder.balanceTextView.text=(MaterialList!![position].balance?:0.00f).toString()

            return view
        }

        override fun getItem(position: Int): Any {
            return MaterialList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return MaterialList!!.size
        }





        fun filter(text: String?) {


            //Our Search text
            val text = text!!.toLowerCase(Locale.getDefault())

            MaterialList?.clear()

            //Here We Clear Both ArrayList because We update according to Search query.



            if (text.length == 0) {

                /*If Search query is Empty than we add all temp data into our main ArrayList

                We store Value in temp in Starting of Program.

                */

                MaterialList?.addAll(tempNameVersionList)


            } else {


                for (i in 0..tempNameVersionList.size - 1) {

                    /*
                    If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                    if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                    */

                    if (tempNameVersionList.get(i).description!!.toLowerCase(Locale.getDefault()).contains(text)) {


                        MaterialList?.add(tempNameVersionList.get(i))


                    }



                }
            }

            //This is to notify that data change in Adapter and Reflect the changes.
            notifyDataSetChanged()


        }


        inner class ViewHolder(view: View?){
            var descrTextView: TextView
            var balanceTextView:TextView

            var position:Int

            init{

                this.descrTextView=view?.findViewById<TextView>(R.id.itemText) as TextView
                this.balanceTextView=view?.findViewById<TextView>(R.id.balanceText) as TextView
                this.position=-1
                view.setOnClickListener { v: View ->

                    //val i = Intent(v.context,InvoiceActivity::class.java)
                    //v.context.startActivity(i)
                    val test =this@Tab1Fragment.frameLayout1
                    test.removeAllViews()

                    //val itemFragment:Fragment = lineItem()
                    val fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
                    val args=Bundle()
                    args.putString("description",MaterialList!![position].description)
                    args.putString("code",MaterialList!![position].code)
                    args.putInt("iteId",MaterialList!![position].erpid)
                    args.putInt("vtcId",MaterialList!![position].vatid)
                    args.putFloat("price",MaterialList!![position].price)
                    args.putFloat("discount",MaterialList!![position].maxdiscount)
                    Log.d("JIM1",MaterialList!![position].vatid.toString())
                    itemFragment.arguments=args
                    //fragmentTransaction.replace(R.id.frameLayout1, itemFragment)
                    fragmentTransaction.replace(R.id.containerFrame1,itemFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    val iteId=MaterialList!![position].erpid
                    listener.onItemSelected(position,iteId)
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
