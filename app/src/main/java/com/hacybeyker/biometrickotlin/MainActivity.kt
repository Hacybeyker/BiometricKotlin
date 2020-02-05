package com.hacybeyker.biometrickotlin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Documentation https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html
        verifySupportsBiometric()
    }

    private fun verifySupportsBiometric() {
        val biometricManager = BiometricManager.from(applicationContext)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            val instanceOfBiometricPrompt = instanceOfBiometricPrompt()
            instanceOfBiometricPrompt.authenticate(getPromptInfo())
        }
    }

    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(applicationContext)
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
            .setTitle("My App's Authentication")
            .setSubtitle("Please login to get access")
            .setDescription("My App is using Android biometric authentication")
            .setDeviceCredentialAllowed(true)
            .setConfirmationRequired(true)
            .build()
    }

    private fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
}
