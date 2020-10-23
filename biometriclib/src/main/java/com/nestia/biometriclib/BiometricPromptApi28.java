package com.nestia.biometriclib;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

/**
 * Created by gaoyang on 2018/06/19.
 */
public class BiometricPromptApi28 implements IBiometricPromptImpl {

    private static final String KEY_NAME = "BiometricPromptApi28";

    private Activity mActivity;
    private BiometricPrompt mBiometricPrompt;
    private BiometricPromptManager.OnBiometricIdentifyCallback mManagerIdentifyCallback;
    private CancellationSignal mCancellationSignal;
    private Signature mSignature;
    private String mToBeSignedMessage;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public BiometricPromptApi28(Activity activity) {
        mActivity = activity;
        mBiometricPrompt = new BiometricPrompt
                .Builder(activity)
                .setTitle(activity.getResources().getString(R.string.biometric_dialog_title))
                .setDescription(activity.getResources().getString(R.string.biometric_dialog_subtitle))
                .setSubtitle("")
                .setNegativeButton(activity.getResources().getString(R.string.biometric_dialog_use_password),
                        activity.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mManagerIdentifyCallback != null) {
                            mManagerIdentifyCallback.onUsePassword();
                        }
                        mCancellationSignal.cancel();
                    }
                })
                .build();


        try {
            KeyPair keyPair = generateKeyPair(KEY_NAME, true);
            // Send public key part of key pair to the server, this public key will be used for authentication
            mToBeSignedMessage = new StringBuilder()
                    .append(Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.URL_SAFE))
                    .append(":")
                    .append(KEY_NAME)
                    .append(":")
                    // Generated by the server to protect against replay attack
                    .append("12345")
                    .toString();

            mSignature = initSignature(KEY_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void authenticate(@Nullable CancellationSignal cancel,
                              BiometricPromptManager.OnBiometricIdentifyCallback callback) {
        mManagerIdentifyCallback = callback;

        mCancellationSignal = cancel;
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
            }
        });

        mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(mSignature),
                mCancellationSignal, mActivity.getMainExecutor(), new BiometricPromptCallbackImpl());
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private class BiometricPromptCallbackImpl extends BiometricPrompt.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            mCancellationSignal.cancel();

        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            mManagerIdentifyCallback.onSucceeded();
            mCancellationSignal.cancel();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }
    }

    /**
     * Generate NIST P-256 EC Key pair for signing and verification
     * @param keyName
     * @param invalidatedByBiometricEnrollment
     * @return
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private KeyPair generateKeyPair(String keyName, boolean invalidatedByBiometricEnrollment) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                // Require the user to authenticate with a biometric to authorize every use of the key
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);

        keyPairGenerator.initialize(builder.build());

        return keyPairGenerator.generateKeyPair();
    }

    @Nullable
    private KeyPair getKeyPair(String keyName) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(keyName)) {
            // Get public key
            PublicKey publicKey = keyStore.getCertificate(keyName).getPublicKey();
            // Get private key
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyName, null);
            // Return a key pair
            return new KeyPair(publicKey, privateKey);
        }
        return null;
    }

    @Nullable
    private Signature initSignature (String keyName) throws Exception {
        KeyPair keyPair = getKeyPair(keyName);

        if (keyPair != null) {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(keyPair.getPrivate());
            return signature;
        }
        return null;
    }

}
