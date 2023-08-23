package com.daikou.p2parking.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import com.daikou.p2parking.BuildConfig
import com.daikou.p2parking.R
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.base.SimpleBaseActivity
import com.daikou.p2parking.databinding.ActivityLoginBinding
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.helper.CustomSetOnClickViewListener
import com.daikou.p2parking.helper.MessageUtils
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.model.User
import com.daikou.p2parking.utility.RedirectClass
import com.daikou.p2parking.view_model.LoginViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException

class LoginActivity : SimpleBaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initAction()
    }

    private fun initView() {
        binding.toolbar.title.text = resources.getString(R.string.log_in).uppercase()

        binding.versionTv.text = String.format("%s %s", resources.getString(R.string.version), BuildConfig.VERSION_NAME)

    }

    private fun phoneNumberValidate(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun initAction() {
        // Observation
        loginViewModel.loadingLoginLiveData.observe(this) {
            binding.loadingView.root.visibility = if (it) View.VISIBLE else View.GONE
        }

        loginViewModel.dataLoginLiveData.observe(this) { respondState ->
            if (respondState.success) {
                respondState.data?.let {
                    val jsonObject = respondState.data.asJsonObject
                    val accessToken = jsonObject["access_token"].asString

                    //new code
                    accessToken?.let { token ->
                        AuthHelper.saveAccessToken(this, token)
                        getUserFromGson(jsonObject)?.let {
                            AuthHelper.saveUserSharePreference(this, it)
                            RedirectClass.gotoMainActivity(self())
                        }
                    } ?: MessageUtils.showError(this, null, "Token is null")
                }
            } else {
                MessageUtils.showError(this, null, respondState.message)
            }
        }

        // Do Action
        binding.actionLoginMtb.setOnClickListener (CustomSetOnClickViewListener {
            // RedirectClass.gotoMainActivity(this)
            val phoneNumber = binding.phoneNumberTf.text.toString()
            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length < Constants.PhoneConfig.PHONE_MIN_LENGTH ||
                phoneNumber.length > Constants.PhoneConfig.PHONE_MAX_LENGTH ||
                !phoneNumberValidate(phoneNumber)
            ) {
                binding.phoneNumberTfLayout.error =
                    getString(R.string.please_enter_a_valid_mobile_number)
            } else if (TextUtils.isEmpty(
                    binding.passwordTf.text.toString().trim()
                ) || binding.passwordTf.text.toString().length < 6
            ) {
                binding.phoneNumberTfLayout.error = null
                binding.passwordTfLayout.error =
                    getString(R.string.please_enter_password)
            } else {
                binding.phoneNumberTfLayout.error = null
                binding.passwordTfLayout.error = null
                val requestBodyMap = HashMap<String, Any>()
                requestBodyMap["phone"] = binding.phoneNumberTf.text.toString().trim()
                requestBodyMap["password"] = binding.passwordTf.text.toString().trim()
                loginViewModel.login(requestBodyMap)
            }
        })
    }

    private fun getUserFromGson(jsonObject: JsonObject): User? {
        try {
            val userObject = jsonObject["user"].asJsonObject
            return Config.GsonConverterHelper.getJsonObjectToGenericClass<User>(userObject.toString())
        } catch (jsonSyntax: JsonSyntaxException) {
            jsonSyntax.printStackTrace()
        }
        return null
    }
}