package gr.algo.algomobilemini

import java.io.Serializable

data class Customer(val name:String="",val address:String?=null,val district:String?=null,val title:String?=null,val afm:String="",val doyid:Int?=null ,val erpid: Int=0,
                    val occupation:String?=null, val  tel1:String?=null,val tel2:String?=null,val fax:String?=null,val email:String?=null,val vatstatusid:Int=0,val city:String?=null,
                    val comments:String?=null, val routeid:Int=0,val erpupd:Int=0,val id:Int=-1):Serializable