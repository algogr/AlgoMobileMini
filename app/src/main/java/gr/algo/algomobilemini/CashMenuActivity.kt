package gr.algo.algomobilemini

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_cash_menu.*

class CashMenuActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerCash.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cash_menu)
//        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this)
        recycler_view1.layoutManager = layoutManager

        adapter = RecyclerCash()
        recycler_view1.adapter = adapter

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}
