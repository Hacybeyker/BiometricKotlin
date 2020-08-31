package com.hacybeyker.biometrickotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.hacybeyker.biometrickotlin.databinding.ActivityMainBinding

//Documentation https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifySupportsBiometric()
        binding.fingerButtonLogin.setOnClickListener {
            val instanceOfBiometricPrompt = instanceOfBiometricPrompt()
            instanceOfBiometricPrompt.authenticate(getPromptInfo())
        }
    }

    private fun verifySupportsBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.fingerTextMessage.text = getString(R.string.biometric_success)
                //binding.fingerTextMessage.setTextColor(Color.parseColor("#Fafafa"))
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.fingerTextMessage.text = getString(R.string.biometric_no_hardware)
                binding.fingerButtonLogin.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.fingerTextMessage.text = getString(R.string.biometric_hw_unavailable)
                binding.fingerButtonLogin.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.fingerTextMessage.text = getString(R.string.biometric_none_enrolled)
                binding.fingerButtonLogin.visibility = View.GONE
            }
        }
    }

    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showMessage("$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showMessage("Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showMessage("Authentication was successful")
            }
        }

        return BiometricPrompt(this, executor, callback)
    }

    private fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login")
            .setDescription("User your fingerprint to login to your app")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
}
