

package gr.algo.algomobilemini

import android.os.Bundle
import android.support.v4.app.FragmentActivity

import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : FragmentActivity() {

    val doylistfragment:DoyListFragment=DoyListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        toolbar.setTitle("Στοιχεία πελάτη")
        //setSupportActionBar(toolbar)

    }

}
