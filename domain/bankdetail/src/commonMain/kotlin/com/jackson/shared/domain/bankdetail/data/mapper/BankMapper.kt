package com.jackson.shared.domain.bankdetail.data.mapper

import com.jackson.shared.api.bankdetail.data.response.BankDetailResponse
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo
import com.jackson.shared.api.bankdetail.data.response.BankListInfoResponse
import com.jackson.shared.api.bankdetail.data.response.BankSwiftResponse
import database.GetBankSwiftByOffset
import database.GetEnabledBankDetailsByOffset
import database.GetFilteredBankDetails
import database.GetFilteredBankSwift
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

fun BankListInfoResponse.toBankEntity() = Banks(id = id, bankName, bankType, bankCode,
    isEnabled = true, offset = 0,  priority = priority, maxSavingsInterest = maxSavingsInterest,
    minSavingsInterest = minSavingsInterest, isSwiftCodeFetched = false, isListFetched = false)

fun BankDetailResponse.toBankDetailEntity(bankId:String, bankName : String, isEnabled:Boolean, priority:Int,) = BankDetail(bankId,
    bankName, city ?: "", state, centre, swift ?: "", branch, district, contact, address,
    bankCode, ifsc, iso3166 ?: "", micr ?: "", priority, upi ?: false, neft, rtgs, imps, isEnabled)

fun BankSwiftResponse.toBankSwiftEntity(bankId:String, bankName:String, isEnabled:Boolean, ) = BankSwift(bankId,
    bankName, city ?:"" , country, countryCode, branch ?:"", swiftCode, isEnabled)

fun Banks.toBankInfo() = BankInfo(this.id, this.bankName, this.bankType,
    minInterest = minSavingsInterest, maxInterest = maxSavingsInterest,
    isEnabled, count = offset)


fun GetEnabledBankDetailsByOffset.toBankDetailInfo() = BankDetailInfo(bankId, bankName, city, ifscCode = IFSCCode, swiftCode, branch)

fun GetBankSwiftByOffset.toBankDetailInfo() = BankDetailInfo(bankId, bankName, city, ifscCode = "", swiftCode, branch)


fun GetFilteredBankDetails.toBankDetailInfo() = BankDetailInfo(bankId, bankName, city, ifscCode = IFSCCode, swiftCode, branch)

fun GetFilteredBankSwift.toBankDetailInfo() = BankDetailInfo(bankId, bankName, city, ifscCode = "", swiftCode, branch)