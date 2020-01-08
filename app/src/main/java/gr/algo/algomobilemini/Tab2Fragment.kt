package gr.algo.algomobilemini


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_tab2.*
import kotlinx.android.synthetic.main.fragment_tab2.view.*
import android.content.Intent
import android.os.Handler
import android.util.Log
//import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil.getOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.lang.Compiler.command
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Tab2Fragment : Fragment() {


    var finDocLines= mutableListOf<FinDocLine>()
    lateinit var selectedItemsListAdapter:FinDocLinesAdapter
    var sumNV:Float=0.00f
    var sumVat:Float=0.00f
    var totalAmount:Float=0.00f
    var vatStatus:Int=-1
    var totalPrQty:Float=0.00f
    var totalSecQty:Float=0.00f
    private  var itemFragment=lineItem()

    var insertListener:OnInsertListener?=null
    var discardListener:OnDiscardListener?=null
    var printListener:OnPrintListener?=null





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.fragment_tab2, container, false)
        val listview = view.findViewById<ListView>(R.id.listView)
        //finDocLines=arguments.getSerializable("list") as MutableList<FinDocLine>
        selectedItemsListAdapter=FinDocLinesAdapter(this.context,finDocLines)
        //val selectedItemsListAdapter=FinDocLinesAdapter(this.context,finDocLines)
        listview.adapter=selectedItemsListAdapter

        view.sumNValueTextView.text=sumNV.toString()
        view.sumVATTextView.text=sumVat.toString()
        view.sumTotalAmountTextView.text=totalAmount.toString()
        view.acceptButton2.isEnabled=false
        view.acceptButton2.setOnClickListener { v:View->
            insertListener?.onInsert(view.sumNValueTextView.text.toString().toFloat(),view.sumVATTextView.text.toString().toFloat(),
                    view.sumTotalAmountTextView.text.toString().toFloat(),totalPrQty,totalSecQty)




        }

        view.printerButton.setOnClickListener{v:View->


            printListener?.onPrint(context)


        }

        view.cancelButton2.setOnClickListener { v:View->
            discardListener?.onDiscard()
        }


        return view
    }




    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


    }

    fun notifyChanges(){
        selectedItemsListAdapter.notifyDataSetChanged()
        var netValue=0f
        var vat=0f
        var firstQty=0f
        var secQty=0f


        for (findocline in finDocLines)
        {
            netValue+=findocline.netValue
            vat+=findocline.vatValue
            firstQty+=findocline.firstQty
            secQty+=findocline.secQty


        }

        sumVat=round2Decimals(vat).toFloat()
        sumNV=round2Decimals(netValue).toFloat()
        totalAmount=round2Decimals(sumVat+sumNV).toFloat()
        totalPrQty=round2Decimals(firstQty).toFloat()
        totalSecQty=round2Decimals(secQty).toFloat()
        view?.sumNValueTextView?.text=sumNV.toString()
        view?.sumVATTextView?.text=sumVat.toString()
        view?.sumTotalAmountTextView?.text=totalAmount.toString()


    }



    fun round2Decimals(value:Float):String{
        val symbols: DecimalFormatSymbols = DecimalFormatSymbols(Locale.US)
        try {
            val df = DecimalFormat("#.##",symbols)
            df.roundingMode = RoundingMode.HALF_DOWN

            return (df.format(value))
        }
        catch (e: Exception){
            val df = DecimalFormat("#,##",symbols)
            df.roundingMode = RoundingMode.HALF_DOWN

            return (df.format(value))

        }
    }


    inner class FinDocLinesAdapter:BaseAdapter{
        private var finDocLinesList:MutableList<FinDocLine>?
        private var context:Context?=null


        constructor(context:Context,finDocLinesList:MutableList<FinDocLine>?):super(){

            this.context=context

            this.finDocLinesList=finDocLinesList


        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: Tab2Fragment.ViewHolder
            val view: View


            if (convertView==null){

                view= layoutInflater.inflate(R.layout.fragment_tab2_item,parent,false)
                holder= ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as Tab2Fragment.ViewHolder
            }


            holder.position=position


            //holder.iteID.text=finDocLinesList!![position].iteID
            holder.iteCode.text=finDocLinesList!![position].iteCode
            holder.iteDescr.text=finDocLinesList!![position].iteDescription
            holder.firstQty.text=finDocLinesList!![position].firstQty.toString()
            holder.price.text=finDocLinesList!![position].price.toString()
            holder.discount.text=finDocLinesList!![position].discount.toString()
            holder.secDiscount.text=finDocLinesList!![position].secDiscount.toString()
            holder.vtcid.text=finDocLinesList!![position].vatValue.toString()
            holder.netValue.text=finDocLinesList!![position].netValue.toString()





            return view
        }



        override fun getItem(position: Int): Any {
            notifyDataSetChanged()
            return finDocLinesList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return finDocLinesList!!.size
        }

    }



    inner class ViewHolder(view: View?){

        var position:Int
        var iteID:Int
        var iteCode:TextView
        var iteDescr:TextView
        var firstQty:TextView
        var price:TextView
        var discount:TextView
        var secDiscount:TextView
        var vtcid:TextView
        var netValue:TextView
        //var vatAmount:TextView


        init{



            this.position=-1
            this.iteID=-1
            this.vtcid=view?.findViewById<TextView>(R.id.vatTextView) as TextView
            this.iteCode=view?.findViewById<TextView>(R.id.codeTextView) as TextView
            this.iteDescr=view?.findViewById<TextView>(R.id.descrTextView) as TextView
            this.firstQty=view?.findViewById<TextView>(R.id.firstQtyTextView) as TextView
            this.price=view?.findViewById<TextView>(R.id.priceTextView) as TextView
            this.discount=view?.findViewById<TextView>(R.id.discountTextView) as TextView
            this.secDiscount=view?.findViewById<TextView>(R.id.discount2TextView) as TextView
            this.vtcid=view?.findViewById<TextView>(R.id.vatTextView) as TextView
            this.netValue=view?.findViewById<TextView>(R.id.netValueTextView) as TextView
            //val vat=this.netValue.toString().toFloat()*vtcid/100
            //this.vatAmount=vat.toString() as TextView


            view?.setOnClickListener {
                v:View ->
                val test =this@Tab2Fragment.frameLayout2
                //test.removeAllViews()

                //val itemFragment:Fragment = lineItem()
                val fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
                val args=Bundle()
                args.putString("description",finDocLines!![position].iteDescription)
                args.putString("code",finDocLines!![position].iteCode)
                args.putInt("iteId",finDocLines!![position].iteID)
                args.putInt("vtcId",finDocLines!![position].vtcID)
                args.putFloat("firstQty",finDocLines!![position].firstQty)
                args.putFloat("price",finDocLines!![position].price)
                args.putFloat("discount",finDocLines!![position].discount)
                args.putFloat("secdiscount",finDocLines!![position].secDiscount)
                args.putFloat("netValue",finDocLines!![position].netValue)
                args.putFloat("vatValue",finDocLines!![position].vatValue)
                args.putInt("vatstatus",vatStatus)
                args.putInt("mode",1)
                args.putInt("position",position)


                itemFragment.basket=this@Tab2Fragment
                itemFragment.arguments=args

                fragmentTransaction.replace(R.id.containerFrame2, itemFragment)
                fragmentTransaction.addToBackStack(null)




                fragmentTransaction.commit()

            }
        }
    }


    fun setOnInsertListener(listener:OnInsertListener){

        this.insertListener=listener


    }

    fun setOnDiscardListener(listener: OnDiscardListener){

        this.discardListener=listener
    }

    fun setOnPrintListener(listener: OnPrintListener){
        this.printListener=listener
    }

    interface OnInsertListener{

        fun onInsert(netValue: Float, vatValue: Float, totalValue: Float, totalFirstQty:Float,totalSecQty:Float):Unit

    }

    interface OnPrintListener{

        fun onPrint(context: Context):Unit

    }


    interface OnDiscardListener{

        fun onDiscard():Unit

    }






}
