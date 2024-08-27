package com.jackson.shared.domain.bankdetail.data.model

data class BankInfo(val id:String, val bankName:String, val bankType:String?,
                    val minInterest:Float?, val maxInterest:Float?,
                    val isEnabled:Boolean, val count:Long)