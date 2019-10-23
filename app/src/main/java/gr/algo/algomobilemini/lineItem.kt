package gr.algo.algomobilemini


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_line_item.*
import kotlinx.android.synthetic.main.fragment_line_item.view.*


class lineItem : Fragment() {

    var erpId:Int?=-1
    var code:String?=""
    var description:String?=""
    var vtcId:Int?=-1
    var firstQty:Float?=0.00f
    var price:Float?=0.00f
    var discount:Float?=0.00f
    var netValue:Float?=0.00f
    var vatValue:Float?=0.00f
    var vatStatus:Int?=-1
    var vatprc:Float=0.00f


    var mode:Int?=0 //0-Insert     1-Edit
    var position:Int=-1

    var acceptListener:OnAcceptListener?=null
    var deleteListener:OnDeleteListener?=null


    lateinit var basket:Tab2Fragment







    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_line_item, container, false)
        // Inflate the layout for this fragment


        val qt = view.firstQtyEdit
        val pr=view.priceEdit
        val di=view.discountEdit
        val nvl=view.netValueTextView
        val vatv=view.vatValueTextView
        vatStatus=arguments?.getInt("vatstatus")




        qt.setOnFocusChangeListener { v, hasFocus ->

            if(!hasFocus)
            {

                nvl.setText(calculateNV(view))
                vatv.setText(calculateVat(view))
            }
        }


        pr.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus)
            {

                nvl.setText(calculateNV(view))
                vatv.setText(calculateVat(view))

            }
        }

        di.setOnFocusChangeListener { v, hasFocus ->
            //if(!hasFocus)
            //{

                nvl.setText(calculateNV(view))
                vatv.setText(calculateVat(view))
            //}
        }




        view.cancelButton2.setOnClickListener { v:View->
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Επιβεβαίωση διαγραφής")
            builder.setMessage("Το είδος θα διαγραφεί. Επιβεβαίωση!") // add the buttons
            builder.setPositiveButton("Αποδοχή", DialogInterface.OnClickListener{dialog,id->

                deleteListener?.onDelete(position)
                val  fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
                fragmentTransaction.replace(R.id.containerFrame2,basket)
                fragmentTransaction.commit()
            })
            builder.setNegativeButton("Ακύρωση", null) // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }




        view.acceptButton2.setOnClickListener {

            v:View ->

            calculateNV(view)
            calculateVat(view)
            val firstqty=firstQtyEdit.text.toString().toFloatOrNull()?:0.00f


            val price=priceEdit.text.toString().toFloatOrNull()?:0.00f


            val discount=discountEdit.text.toString().toFloatOrNull()?:0.00f

            val netvalue=netValueTextView.text.toString().toFloatOrNull()?:0.00f

            if (firstqty>0) {
                val finDocLine = FinDocLine(iteCode = code!!, iteDescription = description!!,
                        firstQty = firstqty, price = price,
                        discount = discount, netValue = netvalue, vatValue = netvalue!!*vatprc!!/100, vtcID = vtcId!!, iteID = erpId!!)

                val tab1fragment: Fragment = Tab1Fragment()
                acceptListener?.onAccept(finDocLine,position)



                val  fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
                if (position==-1) {

                    fragmentTransaction.replace(R.id.containerFrame1, tab1fragment)
                    fragmentTransaction.commit()
                }
                else
                {




                    fragmentTransaction.replace(R.id.containerFrame2,basket)
                    fragmentTransaction.commit()

                    return@setOnClickListener
                }





            }
            else
            {
                Snackbar.make(v, "Δεν καταχωρήθηκε ποσότητα ",
                      Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }





        }








        return view



    }

    override fun onStop() {
        super.onStop()
        calculateVat(this.view!!)
        calculateNV(this.view!!)
        Log.d("JIM","onStop")

    }

    override fun onPause() {
        super.onPause()
        calculateVat(this.view!!)
        calculateNV(this.view!!)
        Log.d("JIM","onPause")
    }







    fun calculateNV(view: View):String{
        val q = view.firstQtyEdit
        val p=view.priceEdit
        val d=view.discountEdit
        val qty=q.text?.toString()?.toFloatOrNull()?:0.00f
        val price=p.text?.toString()?.toFloatOrNull()?:0.00f
        val discount=d.text?.toString()?.toFloatOrNull()?:0.00f
        val rv:Float=qty*price-(qty*price*discount/100)
        view.netValueTextView.text=rv.toString()
        return  rv.toString()
    }

    fun calculateVat(view: View):String{
        val q = view.firstQtyEdit
        val p=view.priceEdit
        val d=view.discountEdit
        val qty=q.text?.toString()?.toFloatOrNull()?:0.00f
        val price=p.text?.toString()?.toFloatOrNull()?:0.00f
        val discount=d.text?.toString()?.toFloatOrNull()?:0.00f
        val rv:Float=qty*price-(qty*price*discount/100)
        val vatvalue=rv*vatprc/100
        view.vatValueTextView.text=vatvalue.toString()
        return  vatvalue.toString()
    }





    override fun onResume() {
        super.onResume()
        Log.d("JIM20","ONRESUME")
        //val t=activity as InvoiceActivity
        // basket=t.basket
        code=arguments?.getString("code")
        description=arguments?.getString("description")
        erpId=arguments?.getInt("iteId")
        vtcId=arguments?.getInt("vtcId")
        mode=arguments?.getInt("mode")
        vatStatus=arguments?.getInt("vatstatus")

        val handler=MyDBHandler(context = this.context,version = 1,name=null,factory = null)
        vatprc=handler.getVatPercent(vtcId.toString(),vatStatus!!)

        textCode?.setText(code)
        textDescr?.setText(description)
        vatPrcTextView.setText(vatprc.toString())
        if (mode==1)
        {
            position=arguments!!.getInt("position")
            firstQty=arguments?.getFloat("firstQty")
            price=arguments?.getFloat("price")
            discount=arguments?.getFloat("discount")
            netValue=arguments?.getFloat("netValue")
            vatValue=arguments?.getFloat("vatValue")

            firstQtyEdit.text=SpannableStringBuilder(firstQty.toString())
            priceEdit.text=SpannableStringBuilder(price.toString())
            discountEdit.text=SpannableStringBuilder(discount.toString())
            netValueTextView.text=netValue.toString()
            vatValueTextView.text=vatValue.toString()
            //vatPrcTextView.text=vatprc.toString()
        }



    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    fun setOnAcceptListener(listener:OnAcceptListener){

        this.acceptListener=listener



    }

    fun setOnDeleteListener(listener:OnDeleteListener){
        this.deleteListener=listener
    }

    interface OnAcceptListener{

        fun onAccept(finDocLine:FinDocLine,position: Int):Unit

    }


    interface OnDeleteListener{

        fun onDelete(position:Int):Unit

    }

}
