package gr.algo.algomobilemini

import java.io.Serializable

data class Material (val code:String,val description:String,val price:Float,
                     val vatid:Int,val maxdiscount: Float,val unit:String,
                     val erpid:Int,val id:Int,val balance:Float?) :Serializable

