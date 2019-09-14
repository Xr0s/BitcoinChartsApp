package br.com.db1.data_remote.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("errors")
    val errorList: List<String>
)