package gr.algo.algomobilemini

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
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
    var mode:Int=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_customer, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        buttonDOY.setText(arguments?.getString("doydescr")?:"ΕΠΙΛΕΞΤΕ ΔΟΥ")
        doyid=arguments?.getInt("doyerpid")


        val myHandler=MyDBHandler(context = this.context,version = 1,name=null,factory = null)
        cusId=activity.intent.extras?.getInt("cusid")
        routeid=activity.intent.extras?.getInt("routeid")
        Log.d("JIM-ROUTEID1",routeid.toString())
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
        else
        {

            if(arguments?.getInt("mode")==1) {
                Log.d("JIM","ELSE")
                editName.text = SpannableStringBuilder(arguments?.getString("cusname"))
                editAddress.text = SpannableStringBuilder(arguments?.getString("address"))
                editDistrict.text = SpannableStringBuilder(arguments?.getString("district"))
                editTitle.text = SpannableStringBuilder(arguments?.getString("title"))
                editAFM.text = SpannableStringBuilder(arguments?.getString("afm"))
                editOccupation.text = SpannableStringBuilder(arguments?.getString("occupation"))
                editPhone1.text = SpannableStringBuilder(arguments?.getString("tel1"))
                editPhone2.text = SpannableStringBuilder(arguments?.getString("tel2"))
                editFax.text = SpannableStringBuilder(arguments?.getString("fax"))
                editEmail.text = SpannableStringBuilder(arguments?.getString("email"))
                switchVat.isChecked = (arguments?.getInt("vatstatusid") == 1)
                editCity.text = SpannableStringBuilder(arguments?.getString("city"))
                editComments.text = SpannableStringBuilder(arguments?.getString("comments"))
                routeid = arguments?.getInt("routeid")
                doyid = arguments?.getInt("doyid")
                buttonDOY.text = arguments.getString("doydescr")
            }

            }



        acceptButton3.setOnClickListener {
            if (cusId==0) {
                val customer = Customer(name = editName.text.toString(), address = editAddress.text.toString(), district = editDistrict.text.toString(), title = editTitle.text.toString(),
                        afm = editAFM.text.toString(), doyid = this.doyid, erpid = this.erpid, occupation = editOccupation.text.toString(), tel1 = editPhone1.text.toString(),
                        tel2 = editPhone2.text.toString(), fax = editFax.text.toString(), email = editEmail.text.toString(), vatstatusid = if (switchVat.isChecked) 1 else 0, city = editCity.text.toString(),
                        comments = editComments.text.toString(), routeid = this.routeid!!, erpupd = 0, id = 0)
                myHandler.insertCustomer(customer)
            }
            else
            {
                val query="UPDATE customer set name = '"+editName.text.toString()+"', address = '"+editAddress.text.toString()+"',district = '"+editDistrict.text.toString()+"', title ='"+editTitle.text.toString()+
                        "',afm = '"+editAFM.text.toString()+"',doyid ="+ this.doyid.toString()+", occupation ='"+ editOccupation.text.toString()+"', tel1 ='"+ editPhone1.text.toString()+
                "',tel2 ='"+ editPhone2.text.toString()+"', fax ='"+ editFax.text.toString()+"', email ='"+ editEmail.text.toString()+"', vatstatusid =" +if (switchVat.isChecked) "1" else "0"+",city ='"+editCity.text.toString()+
                "',comments ='"+editComments.text.toString()+"', routeid ="+this.routeid!!.toString()+", erpupd = 2 where id="+cusId
                myHandler.insertUpdate(query)
            }

            val i = Intent(it.context,CustomerListActivity::class.java)
            i.putExtra("routeid",routeid!!)
            it.context.startActivity(i)


        }
        cancelButton3.setOnClickListener {
            val i = Intent(it.context,CustomerListActivity::class.java)
            i.putExtra("routeid",routeid!!)
            it.context.startActivity(i)

        }


        buttonDOY.setOnClickListener {
            val test = activity as CustomerActivity
            test.CoordinatorLayout1.removeAllViews()
            val fragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
            val args=Bundle()
            args.putString("cusname",editName.text.toString())
            args.putString("address",editAddress.text.toString())
            args.putString("district",editDistrict.text.toString())
            args.putString("title",editTitle.text.toString())
            args.putString("afm",editAFM.text.toString())
            args.putString("occupation",editOccupation.text.toString())
            args.putString("tel1",editPhone1.text.toString())
            args.putString("tel2",editPhone2.text.toString())
            args.putString("fax",editFax.text.toString())
            args.putString("email",editEmail.text.toString())
            args.putInt("vatstatusid",if(switchVat.isChecked()) 1 else 0)
            args.putString("city",editCity.text.toString())
            args.putString("comments",editComments.text.toString())
            args.putInt("routeid",routeid!!)



            val doylistfragment=DoyListFragment()
            doylistfragment.arguments=args
            fragmentTransaction.replace(R.id.CoordinatorLayout1,doylistfragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }




    }

}
