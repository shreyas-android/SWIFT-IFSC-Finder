package com.jackson.shared.api.bankdetail.remote.endpoints


import com.jackson.shared.api.bankdetail.remote.RemoteConstants
import com.jackson.shared.common.bankdetail.remote.client.NetworkClient
import com.jackson.shared.api.bankdetail.remote.path.RemoteUrlPath
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

internal class BankRemoteServiceImpl(private val networkClient : NetworkClient,
                                     private val remoteUrlPath: RemoteUrlPath,): BankRemoteService {

    override suspend fun getAllBanks() : HttpResponse {
       return networkClient.getNetworkClient().get(remoteUrlPath.getBankList())
    }

    override suspend fun getBankDetailFromBankCode(bankCode : String, offset:Long) : HttpResponse {
        return networkClient.getNetworkClient().get(remoteUrlPath.getBankInfoFromBankCode()){
            url{
                parameters.append("bankcode", bankCode)
                parameters.append("offset", offset.toString())
                parameters.append("limit", "100")
            }
        }
    }

    override suspend fun getBankSwiftByBankName(apiKey:String, bankName : String) : HttpResponse {
        return networkClient.getNetworkClient().get(remoteUrlPath.getBankSwift()){
            headers {
                parameter(
                    RemoteConstants.PARAM_BANK_DETAIL_HEADER,
                    apiKey)
            }
           url {
               parameters.append("bank", bankName)
               parameters.append("country", "IN")
           }
        }
    }

}