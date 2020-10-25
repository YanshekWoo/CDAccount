package com.xuexiang.cdaccount.biometriclib;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;

import static com.xuexiang.xui.XUI.getContext;


/**
 * Created by gaoyang on 2018/06/19.
 */
public class BiometricPromptManager {

    private static final String TAG = "BiometricPromptManager";
    private IBiometricPromptImpl mImpl;
//    private final Activity mActivity;

    public interface OnBiometricIdentifyCallback {
        void onUsePassword();

        void onSucceeded();

        void onFailed();

        void onError(int code, String reason);

        void onCancel();

    }

//    @NotNull
//    @Contract("_ -> new")
//    public static BiometricPromptManager from(Activity activity) {
//        return new BiometricPromptManager(activity);
//    }

    public BiometricPromptManager() {
//        mActivity = activity;
        if (isAboveApi28()) {
            mImpl = new BiometricPromptApi28();
        } else if (isAboveApi23()) {
            mImpl = new BiometricPromptApi23();
        }
    }

    private boolean isAboveApi28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    private boolean isAboveApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void authenticate(@NonNull OnBiometricIdentifyCallback callback) {
        mImpl.authenticate(new CancellationSignal(), callback);
    }

    public void authenticate(@NonNull CancellationSignal cancel,
                             @NonNull OnBiometricIdentifyCallback callback) {
        mImpl.authenticate(cancel, callback);
    }

    /**
     * Determine if there is at least one fingerprint enrolled.
     *
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    public boolean hasEnrolledFingerprints() {
        if (isAboveApi28()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager manager = getContext().getSystemService(FingerprintManager.class);
            return manager != null && manager.hasEnrolledFingerprints();
        } else if (isAboveApi23()) {
            return ((BiometricPromptApi23)mImpl).hasEnrolledFingerprints();
        } else {
            return false;
        }
    }

    /**
     * Determine if fingerprint hardware is present and functional.
     *
     * @return true if hardware is present and functional, false otherwise.
     */
    public boolean isHardwareDetected() {
        if (isAboveApi28()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager fm = getContext().getSystemService(FingerprintManager.class);
            return fm != null && fm.isHardwareDetected();
        } else if (isAboveApi23()) {
            return ((BiometricPromptApi23)mImpl).isHardwareDetected();
        } else {
            return false;
        }
    }

    public boolean isKeyguardSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * Whether the device support biometric.
     *
     * @return  API是否23
     */
    public boolean isBiometricPromptEnable() {
        return isAboveApi23()
                && isHardwareDetected()
                && hasEnrolledFingerprints()
                && isKeyguardSecure();
    }

    /**
     * Whether fingerprint identification is turned on in app setting.
     */
    public boolean isBiometricSettingEnable() {
        return SPUtils.getBoolean(getContext(), SPUtils.KEY_BIOMETRIC_SWITCH_ENABLE, false);
    }

    /**
     * Set fingerprint identification enable in app setting.
     */
    public void setBiometricSettingEnable(boolean enable) {
        SPUtils.put(getContext(), SPUtils.KEY_BIOMETRIC_SWITCH_ENABLE, enable);
    }
}
