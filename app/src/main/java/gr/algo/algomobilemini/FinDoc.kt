package gr.algo.algomobilemini

import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class FinDoc ( var id:Int=-1,var cusId: Int=-1,var dsrId:Int=-1,
                    var ftrdate: () -> String ={
    var answer:String
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val current = LocalDateTime.now()
        //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
        answer =  current.format(formatter)
        Log.d("answer",answer)

    } else {
        var date = Date();
        //val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
        val formatter = SimpleDateFormat("MMM dd yyyy")
        answer = formatter.format(date)
        Log.d("answer",answer)

    }
    answer},
                    var dsrNumber: Int=-1,var salesmanId: Int=-1,var comments:String?="",
                    var deliveryAddress:String?="a",var erpUpd:Int=0,var netValue:Float=0.00f,var vatAmount:Float=0.00f,var totAmount:Float=0.00f,var vatStatus:Int=-1,var isCash:Int=0)
