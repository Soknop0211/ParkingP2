package com.daikou.p2parking.helper

import android.content.Context
import com.daikou.p2parking.data.di.network.CustomHttpLogging
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object AuthHelper{

    fun hasUserInSharePreference(context: Context) : Boolean{
        return HelperUtil.getStringSharePreference(context, Constants.Auth.USER_KEY) != ""
    }
    fun saveUserSharePreference(context: Context, user: User){
        val userJson = Gson().toJson(user)
        HelperUtil.setStringSharePreference(context, Constants.Auth.USER_KEY, userJson)
    }
    fun getUserSharePreference(context: Context): User{
        val userJson = HelperUtil.getStringSharePreference(context, Constants.Auth.USER_KEY)
        return if(userJson!= ""){
            val typeToken = object : TypeToken<User>(){}.type
            val gson = Gson()
            gson.fromJson(userJson, typeToken)
        }else{
            User()
        }
    }

    fun saveAccessToken(context: Context, token : String){
        HelperUtil.setStringSharePreference(context, Constants.Auth.TOKEN_KEY, token)
    }

    fun getAccessToken(context: Context) : String{
        return HelperUtil.getStringSharePreference(context, Constants.Auth.TOKEN_KEY)
    }

    fun clearSession(context: Context){
        HelperUtil.setStringSharePreference(context, Constants.Auth.USER_KEY, "")
        HelperUtil.setStringSharePreference(context, Constants.Auth.TOKEN_KEY, "")
    }

}