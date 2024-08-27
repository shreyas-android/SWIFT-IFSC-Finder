package com.jackson.shared.data.bankdetail


import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.jackson.shared.data.bankdetail.database.BankDatabase
import com.jackson.shared.data.bankdetail.datasource.BankDataSourceImpl
import com.jackson.shared.data.bankdetail.repository.BankDataRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import migrations.BankDetail
import migrations.Banks

internal class Instance(private val driver: SqlDriver) {

    private fun getDatabase(): BankDatabase {
        val priorityAdapter = object: ColumnAdapter<Int, Long>{
            override fun decode(databaseValue : Long) : Int {
                return databaseValue.toInt()
            }

            override fun encode(value : Int) : Long {
                return value.toLong()
            }

        }

        val interestAdapter = object :ColumnAdapter<Float,Double>{
            override fun decode(databaseValue : Double) : Float {
                return databaseValue.toFloat()
            }

            override fun encode(value : Float) : Double {
                return  value.toDouble()
            }

        }
        return BankDatabase(driver, BankDetail.Adapter(priorityAdapter),
            Banks.Adapter(priorityAdapter = priorityAdapter,
            minSavingsInterestAdapter = interestAdapter, maxSavingsInterestAdapter = interestAdapter),
           )
    }

    private val bankDatabase by lazy {
        getDatabase()
    }

    private val dispatcher by lazy {
        Dispatchers.IO
    }

    private val bankDataSource by lazy {
        BankDataSourceImpl(dispatcher, bankDatabase.bankQueries, bankDatabase.bankdetailQueries,
            bankDatabase.bankswiftQueries)
    }

    val bankDataRepository by lazy {
        BankDataRepositoryImpl(bankDataSource)
    }
}