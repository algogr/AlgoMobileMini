package gr.algo.algomobilemini

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_invoice_list.*

import kotlinx.android.synthetic.main.content_invoice_list.*

class InvoiceListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_invoice_list)
        toolbar.setTitle("Παραστατικά")

        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)

        val invoiceList:MutableList<FinDoc>? =handler.getInvoices()

        val invoiceListAdapter=InvoiceListViewAdapter(this@InvoiceListActivity,invoiceList)


        invoicelistview.adapter=invoiceListAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i= Intent(this,ReportsMenuActivity::class.java)

        startActivity(i)
    }


    inner class InvoiceListViewAdapter: BaseAdapter {

        private var invoiceList:MutableList<FinDoc>?
        private  var context: Context? =null
        private  val mInflator: LayoutInflater
        private var hashMapTexts:  HashMap<String,String>



        constructor(context: Context, invoicelist: MutableList<FinDoc>?) : super(){
            this.invoiceList=invoicelist
            this.context=context
            this.hashMapTexts= HashMap()
            this.mInflator= LayoutInflater.from(context)
            for ( invoice in invoiceList!!) {

                this.hashMapTexts.put(invoice.dsrNumber.toString(),"")

            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: InvoiceListActivity.ViewHolder
            val view: View

            if (convertView==null){
                view= layoutInflater.inflate(R.layout.invoice_list_item,parent,false)
                holder=ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as ViewHolder
            }


            holder.position=position
            holder.dateText.text=invoiceList!![position].ftrdate.toString()
            holder.dsrNumberText.text=invoiceList!![position].dsrNumber.toString()

            holder.cusNameText.text=invoiceList!![position].customer.name
            holder.totalText.text=invoiceList!![position].totAmount.toString()
            holder.shortDescrText.text=invoiceList!![position].shortDescr.toString()
            holder.ftrId=invoiceList!![position].id
            return view
        }

        override fun getItem(position: Int): Any {
            return invoiceList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return invoiceList!!.size
        }


    }


    private class ViewHolder(view: View?){
        var dateText:TextView
        var dsrNumberText:TextView
        var cusNameText:TextView
        var totalText:TextView
        var shortDescrText:TextView


        var position:Int=-1

        var ftrId:Int=-1


        init {

                this.position = -1
                this.dateText=view?.findViewById<TextView>(R.id.dateText) as TextView
                this.dsrNumberText=view?.findViewById<TextView>(R.id.dsrnumberText) as TextView
                this.cusNameText=view?.findViewById<TextView>(R.id.customerText) as TextView
                this.totalText=view?.findViewById<TextView>(R.id.totalText) as TextView
                this.shortDescrText=view?.findViewById<TextView>(R.id.shortdescrText) as TextView
                view?.setOnClickListener { v: View ->



                val i = Intent(v.context, InvoiceActivity::class.java)
                i.putExtra("mode", 1)
                i.putExtra("ftrid",ftrId)
                v.context.startActivity(i)
            }

        }

    }

}
