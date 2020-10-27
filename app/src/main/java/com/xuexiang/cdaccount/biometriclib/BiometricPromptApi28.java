package com.xuexiang.cdaccount.biometriclib;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.xuexiang.cdaccount.R;

import org.jetbrains.annotations.NotNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import static com.xuexiang.xui.XUI.getContext;

/**
 * Created by gaoyang on 2018/06/19.
 */
public class BiometricPromptApi28 implements IBiometricPromptImpl {

    private static final String KEY_NAME = "BiometricPromptApi28";

    private BiometricPrompt mBiometricPrompt;
    private BiometricPromptManager.OnBiometricIdentifyCallback mManagerIdentifyCallback;
    private CancellationSignal mCancellationSignal;
    private Signature mSignature;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public BiometricPromptApi28() {
        mBiometricPrompt = new BiometricPrompt
                .Builder(getContext())
                .setTitle(getContext().getResources().getString(R.string.biometric_dialog_title))
                .setDescription(getContext().getResources().getString(R.string.biometric_dialog_subtitle))
                .setSubtitle("")
                .setNegativeButton(getContext().getResources().getString(R.string.biometric_dialog_use_password),
                        getContext().getMainExecutor(), (dialogInterface, i) -> {
                        })
//                .setNegativeButton(getContext().getResources().getString(R.string.biometric_dialog_use_password),
//                        getContext().getMainExecutor(), (dialogInterface, i) -> {
//                            if (mManagerIdentifyCallback != null) {
//                                mManagerIdentifyCallback.onUsePassword();
//                            }
//                            mCancellationSignal.cancel();
//                        })
                .build();


        try {
            KeyPair keyPair = generateKeyPair();
            // Send public key part of key pair to the server, this public key will be used for authentication
            // Generated by the server to protect against replay attack
            String mToBeSignedMessage = new StringBuilder()
                    .append(Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.URL_SAFE))
                    .append(":")
                    .append(KEY_NAME)
                    .append(":")
                    // Generated by the server to protect against replay attack
                    .append("12345")
                    .toString();

            mSignature = initSignature();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void authenticate(@Nullable CancellationSignal cancel,
                             @NotNull BiometricPromptManager.OnBiometricIdentifyCallback callback) {
        mManagerIdentifyCallback = callback;

        mCancellationSignal = cancel;
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        mCancellationSignal.setOnCancelListener(() -> {
        });

        mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(mSignature),
                mCancellationSignal, getContext().getMainExecutor(), new BiometricPromptCallbackImpl());
    }

    @Override
    public void destroy() {
        mBiometricPrompt = null;
        mManagerIdentifyCallback = null;
        mCancellationSignal = null;
        mSignature = null;
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
     * @return KeyPair
     * @throws Exception  ds
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(BiometricPromptApi28.KEY_NAME,
                KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                // Require the user to authenticate with a biometric to authorize every use of the key
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(true);

        keyPairGenerator.initialize(builder.build());

        return keyPairGenerator.generateKeyPair();
    }

    @Nullable
    private KeyPair getKeyPair() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(BiometricPromptApi28.KEY_NAME)) {
            // Get public key
            PublicKey publicKey = keyStore.getCertificate(BiometricPromptApi28.KEY_NAME).getPublicKey();
            // Get private key
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(BiometricPromptApi28.KEY_NAME, null);
            // Return a key pair
            return new KeyPair(publicKey, privateKey);
        }
        return null;
    }

    @Nullable
    private Signature initSignature() throws Exception {
        KeyPair keyPair = getKeyPair();

        if (keyPair != null) {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(keyPair.getPrivate());
            return signature;
        }
        return null;
    }





}
