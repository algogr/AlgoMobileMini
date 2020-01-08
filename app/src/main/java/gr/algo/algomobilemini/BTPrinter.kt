package gr.algo.algomobilemini

import android.content.Context

interface BTPrinter {
    fun findBT()
    fun openBT(context: Context)
    fun closeBT()
    fun invoiceData(findoc:FinDoc,customer: Customer,finDocLines:MutableList<FinDocLine>,company:Company)


}