/*
 * Copyright (c) 2010-2020 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.activities.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.linphone.LinphoneApplication
import org.linphone.LinphoneApplication.Companion.coreContext
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.main.viewmodels.StatusViewModel
import org.linphone.core.CoreContext
import org.linphone.core.tools.Log
import org.linphone.databinding.StatusFragmentBinding
import org.linphone.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class StatusFragment : GenericFragment<StatusFragmentBinding>() {
    private lateinit var viewModel: StatusViewModel

    @SuppressLint("StaticFieldLeak")
    lateinit var coreContext: CoreContext

    lateinit var textView: TextView

    override fun getLayoutId(): Int = R.layout.status_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        useMaterialSharedAxisXForwardAnimation = false

        viewModel = ViewModelProvider(this)[StatusViewModel::class.java]
        binding.viewModel = viewModel

        sharedViewModel.accountRemoved.observe(
            viewLifecycleOwner
        ) {
            Log.i("[Status Fragment] An account was removed, update default account state")
            val defaultAccount = coreContext.core.defaultAccount
            if (defaultAccount != null) {
                viewModel.updateDefaultAccountRegistrationStatus(defaultAccount.state)
            }
        }

        binding.setMenuClickListener {
            sharedViewModel.toggleDrawerEvent.value = Event(true)
        }

        binding.setRefreshClickListener {
            viewModel.refreshRegister()
        }

        textView = view.findViewById<TextView>(R.id.balanceText)

        textView.setOnClickListener {
            // Code to be executed when the TextView is clicked
            // For example, you can perform some action or navigate to another screen
            getBalance()
        }

        getBalance()

        startRepeatingTask()
    }

    fun getBalance() {
        try {
            val localAddress =
                LinphoneApplication.coreContext.core.defaultAccount?.params?.identityAddress?.asStringUriOnly()

            val userName = localAddress?.let { extractSipUsernameFromUri(it) }
            Log.e("localAddress", userName)
            val apiService = RetrofitClient.apiService
            val call = apiService.getBalance(
                extractSipUsernameFromUri(localAddress.toString()).toString()
            )

            call.enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(
                    call: Call<BalanceResponse>,
                    response: Response<BalanceResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val balanceResponse = response.body()

                            if (balanceResponse != null) {
                                if (balanceResponse.result == "success") {
                                    viewModel.balance = "BDT " + balanceResponse.credit
                                    textView.text = "BDT " + balanceResponse.credit
                                }
                            }

                            // Log the response
                            Log.d(
                                "ApiRequest",
                                "Result: ${balanceResponse?.result}, Error: ${balanceResponse?.error}, Username: ${balanceResponse?.username}, Credit: ${balanceResponse?.credit}"
                            )
                        } else {
                            // Log the error
                            Log.e("ApiRequest", "Error: ${response.code()}")
                            showToast("Error: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("ApiRequest", "Exception in onResponse: ${e.message}")
                        showToast("Exception in onResponse: ${e.message}")
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    // Log the failure
                    Log.e("ApiRequest", "Failure: ${t.message}")
                    showToast("Failure: ${t.message}")
                }
            })
        } catch (e: Exception) {
            showToast("Exception in getBalance: ${e.message}")
            Log.e("ApiRequest", "Exception in getBalance: ${e.message}")
        }
    }

    fun extractSipUsernameFromUri(uri: String): String? {
        val startIndex = uri.indexOf(':') + 1
        val endIndex = uri.indexOf('@')
        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            uri.substring(startIndex, endIndex)
        } else {
            null
        }
    }
    private var job: Job? = null // Job to hold the coroutine
    private fun startRepeatingTask() {
        // Launch a coroutine in the lifecycle-aware scope of the activity
        job = lifecycleScope.launch {
            // Repeat indefinitely
            while (true) {
                // Perform your task here
                println("Task executed at ${System.currentTimeMillis()}")
                Log.e("CronJob", "Task executed at ${System.currentTimeMillis()}")
                getBalance()
                // Delay for 5 seconds
                delay(10000) // Delay for 5 seconds (5000 milliseconds)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine when the activity is destroyed
        job?.cancel()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

data class BalanceResponse(
    val result: String,
    val error: String,
    val username: String,
    val credit: String
)

interface ApiService {
    @GET("wb-admin/getBalance.php")
    fun getBalance(@Query("username") username: String): Call<BalanceResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://iptsp.weblinkltd.com/"

    val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Add CoroutineCallAdapterFactory
            .build()

        retrofit.create(ApiService::class.java)
    }
}
