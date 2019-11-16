package gr.algo.algomobilemini


import java.io.Serializable

data class FinDoc ( var id:Int=-1,var cusId: Int=-1,var dsrId:Int=-1,
                    var ftrdate: String ,
                    var dsrNumber: Int=-1,var salesmanId: Int=-1,var comments:String?="",
                    var deliveryAddress:String?="a",var erpUpd:Int=0,var netValue:Float=0.00f,var vatAmount:Float=0.00f,var totAmount:Float=0.00f,var vatStatus:Int=-1,var isCash:Int=0,
                    var shortDescr:String="",var typeDescr:String="",var subErpid:Int=0):Serializable
