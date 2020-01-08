package gr.algo.algomobilemini


import java.io.Serializable
import java.net.Inet4Address

data class FinDoc ( var id:Int=-1,var dsrId:Int=-1,
                    var ftrdate: String ,
                    var dsrNumber: Int=-1,var salesmanId: Int=-1,var comments:String?=null,
                    var deliveryAddress:String?=null,var erpUpd:Int=0,var netValue:Float=0.00f,var vatAmount:Float=0.00f,var totAmount:Float=0.00f,var isCash:Int=0,
                    var shortDescr:String="",var typeDescr:String="",var subsidiary: Subsidiary?=null,var shpToPerId:Int?=null,var shpToAddid:Int?=null,var customer: Customer=Customer(),
                    var totFirstQty:Float=0.00f,var totSecQty:Float=0.00f,var third:Customer?=null,var copies:Int=0):Serializable
