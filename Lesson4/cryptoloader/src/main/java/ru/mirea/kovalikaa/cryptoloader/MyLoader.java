package ru.mirea.kovalikaa.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public	class	MyLoader	extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    private byte[] encryptedText;
    private byte[] key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null)
            encryptedText = args.getByteArray(ARG_WORD);
            key = args.getByteArray("key");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        SystemClock.sleep(5000);
        SecretKey originKey = new SecretKeySpec(key, 0, key.length, "AES");
        return decryptMsg(encryptedText, originKey);
    }

    public	static	String	decryptMsg(byte[]	cipherText,	SecretKey secret){
        /*	Decrypt	the	message	*/
        try	{
            Cipher	cipher	=	Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,	secret);
            return	new	String(cipher.doFinal(cipherText));
        }	catch	(NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                       | BadPaddingException | InvalidKeyException e)	{
            throw	new	RuntimeException(e);
        }
    }
}