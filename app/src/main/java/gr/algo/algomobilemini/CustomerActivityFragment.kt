package gr.algo.algomobilemini

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.fragment_customer.*

/**
 * A placeholder fragment containing a simple view.
 */
class CustomerActivityFragment : Fragment() {

    var doyid:Int?=null
    var erpid:Int=0
    var routeid:Int?=0
    var cusId:Int?=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_customer, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        buttonDOY.setText(arguments?.getString("description")?:"ΕΠΙΛΕΞΤΕ ΔΟΥ")
        doyid=arguments?.getInt("doyerpid")


        val myHandler=MyDBHandler(context = this.context,version = 1,name=null,factory = null)
        cusId=activity.intent.extras?.getInt("cusid")
        routeid=activity.intent.extras?.getInt("routeid")
        if (cusId!!>0)
        {
            val customer=myHandler.getCustomerById(cusId!!)
            editName.text=SpannableStringBuilder(customer.name)
            editAddress.text=SpannableStringBuilder(customer.address)
            editDistrict.text=SpannableStringBuilder(customer.district)
            editTitle.text=SpannableStringBuilder(customer.title)
            editAFM.text=SpannableStringBuilder(customer.afm)
            editOccupation.text=SpannableStringBuilder(customer.occupation)
            editPhone1.text=SpannableStringBuilder(customer.tel1)
            editPhone2.text=SpannableStringBuilder(customer.tel2)
            editFax.text=SpannableStringBuilder(customer.fax)
            editEmail.text=SpannableStringBuilder(customer.email)
            switchVat.isChecked=(customer.vatstatusid==1)
            editCity.text=SpannableStringBuilder(customer.city)
            editComments.text=SpannableStringBuilder(customer.comments)
            routeid=customer.routeid
            if (doyid==null) {
                doyid=customer.doyid
                buttonDOY.text=myHandler.getDoybyErpid(doyid!!).description
            }

        }

        acceptButton3.setOnClickListener {
            if (cusId==0) {
                val customer = Customer(name = editName.text.toString(), address = editAddress.text.toString(), district = editDistrict.text.toString(), title = editTitle.text.toString(),
                        afm = editAFM.text.toString(), doyid = this.doyid!!, erpid = this.erpid, occupation = editOccupation.text.toString(), tel1 = editPhone1.text.toString(),
                        tel2 = editPhone2.text.toString(), fax = editFax.text.toString(), email = editEmail.text.toString(), vatstatusid = if (switchVat.isChecked) 1 else 0, city = editCity.text.toString(),
                        comments = editComments.text.toString(), routeid = this.routeid!!, erpupd = 0, id = 0)
                myHandler.insertCustomer(customer)
            }
            else
            {
                val query="UPDATE customer set name = '"+editName.text.toString()+"', address = '"+editAddress.text.toString()+"',district = '"+editDistrict.text.toString()+"', title ='"+editTitle.text.toString()+
                        "',afm = '"+editAFM.text.toString()+"',doyid ="+ this.doyid.toString()+", occupation ='"+ editOccupation.text.toString()+"', tel1 ='"+ editPhone1.text.toString()+
                "',tel2 ='"+ editPhone2.text.toString()+"', fax ='"+ editFax.text.toString()+"', email ='"+ editEmail.text.toString()+"', vatstatusid =" +if (switchVat.isChecked) "1" else "0"+",city ='"+editCity.text.toString()+
                "',comments ='"+editComments.text.toString()+"', routeid ="+this.routeid!!.toString()+", erpupd = 1 where id="+cusId
                myHandler.insertUpdate(query)
            }

            val i = Intent(it.context,CustomerListActivity::class.java)
            i.putExtra("routeid",routeid)
            it.context.startActivity(i)


        }
        cancelButton3.setOnClickListener {
            val i = Intent(it.context,CustomerListActivity::class.java)
            i.putExtra("routeid",routeid)
            it.context.startActivity(i)

        }


        buttonDOY.setOnClickListener {
            val test = activity as CustomerActivity
            test.CoordinatorLayout1.removeAllViews()
            val fragmentTransaction = activity.getSupportFragmentManager().beginTransaction()


            val doylistfragment=DoyListFragment()
            fragmentTransaction.replace(R.id.CoordinatorLayout1,doylistfragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }




    }

}
