package com.jackson.android.bank.detail.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.jackson.android.bank.detail.R
import com.jackson.android.bank.detail.data.enums.ScreenType
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import java.util.Random

object BankProUtils {

    const val TYPE_FILTER_SEARCH_CITY = 1
    const val TYPE_FILTER_SEARCH_STATE = 2
    const val TYPE_FILTER_SEARCH_BANK_CODE = 3
    const val TYPE_FILTER_SEARCH_DISTRICT = 4
    const val TYPE_FILTER_SEARCH_IFSC_CODE = 5
    const val TYPE_FILTER_SEARCH_BRANCH = 6
    const val TYPE_FILTER_BANK_NAME = 7
    const val TYPE_FILTER_SEARCH_SWIFT_CODE = 8



    fun getAllFilterList(screenType : ScreenType) = if(screenType == ScreenType.IFSC){
        getIFSCFilterList()
    } else{
        getSwiftFilterList()
    }

    private fun getIFSCFilterList() = listOf(TYPE_FILTER_SEARCH_CITY, TYPE_FILTER_SEARCH_STATE, TYPE_FILTER_SEARCH_DISTRICT,
        TYPE_FILTER_SEARCH_BRANCH, TYPE_FILTER_SEARCH_IFSC_CODE,
        TYPE_FILTER_SEARCH_BANK_CODE)

    private fun getSwiftFilterList() = listOf(TYPE_FILTER_SEARCH_CITY, TYPE_FILTER_SEARCH_BRANCH,
        TYPE_FILTER_SEARCH_SWIFT_CODE)

    fun getBankSwiftFilter() = listOf(TYPE_FILTER_SEARCH_CITY, TYPE_FILTER_BANK_NAME, TYPE_FILTER_SEARCH_BRANCH)


    fun getNameByType(context : Context, type:Int):String{
      return  when(type){
            TYPE_FILTER_SEARCH_CITY -> {
                context.getString(R.string.title_city)
            }

            TYPE_FILTER_SEARCH_STATE -> {
                context.getString(R.string.title_state)
            }
            TYPE_FILTER_SEARCH_BANK_CODE -> {
                context.getString(R.string.title_bank_code)
            }

            TYPE_FILTER_SEARCH_DISTRICT -> {
                context.getString(R.string.title_district)
            }

            TYPE_FILTER_SEARCH_IFSC_CODE -> {
                context.getString(R.string.title_ifsc_code)
            }

          TYPE_FILTER_SEARCH_SWIFT_CODE -> {
              context.getString(R.string.title_swift_code)
                  }

          TYPE_FILTER_SEARCH_BRANCH -> {
              context.getString(R.string.title_branch)
          }

          TYPE_FILTER_BANK_NAME -> {
              context.getString(R.string.title_bank_name)
          }

          else -> {
              ""
          }
      }
    }


    fun getFilterValue(filterType:Int, bankFilterInfo : BankFilterInfo) : String {
        return  when(filterType){
            TYPE_FILTER_SEARCH_CITY -> {
                bankFilterInfo.city
            }

            TYPE_FILTER_SEARCH_STATE -> {
                bankFilterInfo.state
            }
            TYPE_FILTER_SEARCH_BANK_CODE -> {
                bankFilterInfo.bankCode
            }

            TYPE_FILTER_SEARCH_DISTRICT -> {
                bankFilterInfo.district
            }

            TYPE_FILTER_SEARCH_IFSC_CODE -> {
                bankFilterInfo.ifscCode
            }

            TYPE_FILTER_SEARCH_BRANCH -> {
                bankFilterInfo.branch
            }

            TYPE_FILTER_BANK_NAME -> {
                bankFilterInfo.bankName
            }
            TYPE_FILTER_SEARCH_SWIFT_CODE -> {
                bankFilterInfo.swiftCode
            }
            else -> {
                bankFilterInfo.bankName
            }
        }
    }

    fun checkAndUpdateValue(filterType : Int, bankFilterInfo : BankFilterInfo, value:String) : BankFilterInfo {

        return when(filterType) {
            TYPE_FILTER_SEARCH_CITY -> {
                bankFilterInfo.copy(city = value)
            }

            TYPE_FILTER_SEARCH_STATE -> {
                bankFilterInfo.copy(state = value)
            }

            TYPE_FILTER_SEARCH_BANK_CODE -> {
                bankFilterInfo.copy(bankCode = value)
            }

            TYPE_FILTER_SEARCH_DISTRICT -> {
                bankFilterInfo.copy(district = value)
            }

            TYPE_FILTER_SEARCH_IFSC_CODE -> {
                bankFilterInfo.copy(ifscCode = value)
            }

            TYPE_FILTER_SEARCH_BRANCH -> {
                bankFilterInfo.copy(branch = value)
            }

            TYPE_FILTER_BANK_NAME -> {
                bankFilterInfo.copy(bankName = value)
            }

            TYPE_FILTER_SEARCH_SWIFT_CODE -> {
                bankFilterInfo.copy(swiftCode = value)
            }

            else -> {
                bankFilterInfo.copy(bankName = value)
            }
        }
    }

    fun checkAndUpdateValue(filterType : Int, swiftCodeFilterInfo : SwiftCodeFilterInfo, value:String) : SwiftCodeFilterInfo {

        return when(filterType) {
            TYPE_FILTER_SEARCH_CITY -> {
                swiftCodeFilterInfo.copy(city = value)
            }


            TYPE_FILTER_SEARCH_BRANCH -> {
                swiftCodeFilterInfo.copy(branch = value)
            }

            TYPE_FILTER_BANK_NAME -> {
                swiftCodeFilterInfo.copy(bankName = value)
            }

            else -> {
                swiftCodeFilterInfo.copy(bankName = value)
            }
        }
    }

    fun copyAndShowToast(context : Context, content:String){

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(Random().nextLong().toString(), content)
        clipboard.setPrimaryClip(data)

        Toast.makeText(context, context.getString(R.string.desc_code_copied), Toast.LENGTH_SHORT).show()

    }

}