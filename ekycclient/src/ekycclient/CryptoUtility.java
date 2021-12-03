package ekycclient;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoUtility {
	private static SecureRandom secureRandom;
	private static final String SYM_ALGORITHM = "AES";
	private static final String AES = "AES";
	private static int tagLength=128; 
	/** The Constant SYM_ALGORITHM_LENGTH. */
	private static final int SYM_ALGORITHM_LENGTH = 256;
	private static BouncyCastleProvider bouncyCastleProvider;
	private static final String MGF1 = "MGF1";

    private static final String HASH_ALGO = "SHA-256";

    private static MessageDigest messageDigest;
	static {
		bouncyCastleProvider = addProvider();
		secureRandom = new SecureRandom();
		try {
			messageDigest =	MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String digestAsPlainText(byte[] data) {
		return DatatypeConverter.printHexBinary(data).toUpperCase();
	}
	public static synchronized byte[] generateHash(final byte[] bytes) {
		return messageDigest.digest(bytes);
	}
	private static BouncyCastleProvider addProvider() {
		BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
		Security.addProvider(bouncyCastleProvider);
		return bouncyCastleProvider;
	}
	public static SecretKey genSecKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen;
		SecretKey secretKey = null;
        keyGen = KeyGenerator.getInstance(CryptoUtility.SYM_ALGORITHM, bouncyCastleProvider);
		keyGen.init(CryptoUtility.SYM_ALGORITHM_LENGTH, new SecureRandom());
		secretKey = keyGen.generateKey();
        return secretKey;

	}
	public static byte[] symmetricEncrypt(byte[] data, SecretKey secretKey, byte[] aad) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
		//return cryptoCore.symmetricEncrypt(secretKey, data, null);

		 Cipher cipher;
		 
	     //cipher = Cipher.getInstance("AES/GCM/NoPadding"); //"AES/GCM/PKCS5Padding"
	     
	     cipher = Cipher.getInstance("AES/GCM/NoPadding"); //"AES/GCM/PKCS5Padding"
	     
	    
	     
	     byte[] output = null;
	     byte[] randomIV = generateIV(cipher.getBlockSize());
	     SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
         GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(tagLength, randomIV);
         cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
         output = new byte[cipher.getOutputSize(data.length) + cipher.getBlockSize()];
         if (aad != null && aad.length != 0) {
             cipher.updateAAD(aad);
         }
         byte[] processData = doFinal(data, cipher);
         System.arraycopy(processData, 0, output, 0, processData.length);
         System.arraycopy(randomIV, 0, output, processData.length, randomIV.length);
         return output;
	}
	private static byte[] generateIV(int blockSize) {
	        byte[] byteIV = new byte[blockSize];
	        secureRandom.nextBytes(byteIV);
	        return byteIV;
	 }
	 private static byte[] doFinal(byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
		 return cipher.doFinal(data);
	 }
	 public static byte[] asymmetricEncrypt(PublicKey key, byte[] data) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		 Cipher cipher;
	     
	     cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
	     final OAEPParameterSpec oaepParams = new OAEPParameterSpec(HASH_ALGO, MGF1, MGF1ParameterSpec.SHA256,
	                PSpecified.DEFAULT);
	     cipher.init(Cipher.ENCRYPT_MODE, key, oaepParams);
	     return doFinal(data, cipher);
	    }

}
