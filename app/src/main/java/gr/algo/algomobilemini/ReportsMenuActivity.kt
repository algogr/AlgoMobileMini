package gr.algo.algomobilemini

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_cash_menu.*

import kotlinx.android.synthetic.main.activity_reports_menu.*

class ReportsMenuActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerReports.ViewHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports_menu)


        layoutManager = LinearLayoutManager(this)
        recycler_view2.layoutManager = layoutManager

        adapter = RecyclerReports()
        recycler_view2.adapter = adapter

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }




}
