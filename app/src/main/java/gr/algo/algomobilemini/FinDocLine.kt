package gr.algo.algomobilemini

data class FinDocLine (val id:Int=-1, val ftrId:Int=-1, val iteID:Int=-1, val iteCode:String="", val iteDescription:String="",
                       var firstQty:Float=0.00f, var secQty:Float=0.00f, var price:Float=0.00f,
                       var discount:Float=0.00f, val vtcID:Int=-1, var vatValue:Float=0.00f, var netValue:Float=0.00f,val muDescr:String="")