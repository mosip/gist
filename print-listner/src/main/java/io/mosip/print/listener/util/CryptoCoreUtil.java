package io.mosip.print.listener.util;

import static java.util.Arrays.copyOfRange;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.print.listener.exception.CryptoManagerException;
import io.mosip.print.listener.exception.PlatformErrorMessages;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CryptoCoreUtil {

	private final static String RSA_ECB_OAEP_PADDING = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

	private final static int THUMBPRINT_LENGTH = 32;

	@Value("${mosip.print.prependThumbprint:false}")
	private boolean isThumbprint;

	@Autowired
	private ApplicationContext applicationContext;

	public String decrypt(String data) throws Exception {
		PrivateKeyEntry privateKeyEntry = loadP12();
		byte[] dataBytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);
		byte[] data1 = decryptData(dataBytes, privateKeyEntry);
		String strData = new String(data1);
		return strData;
	}

	public PrivateKeyEntry loadP12() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, UnrecoverableEntryException {
		KeyStore mosipKeyStore = KeyStore.getInstance("PKCS12");
		InputStream in = getClass().getClassLoader().getResourceAsStream(applicationContext.getPartnerResourCeBundle().getString("partner.private.key.filename"));
		mosipKeyStore.load(in, applicationContext.getPartnerResourCeBundle().getString("partner.private.key.password").toCharArray());
		ProtectionParameter password = new PasswordProtection(applicationContext.getPartnerResourCeBundle().getString("partner.private.key.password").toCharArray());
		PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) mosipKeyStore.getEntry(applicationContext.getPartnerResourCeBundle().getString("partner.private.key.alias"), password);
		return privateKeyEntry;
	}

	public byte[] decryptData(byte[] requestData, PrivateKeyEntry privateKey) throws Exception {
		String keySplitter = "#KEY_SPLITTER#";
		SecretKey symmetricKey = null;
		byte[] encryptedData = null;
		byte[] encryptedSymmetricKey = null;
		final int cipherKeyandDataLength = requestData.length;
		final int keySplitterLength = keySplitter.length();

		int keyDemiliterIndex = getSplitterIndex(requestData, 0, keySplitter);
		byte[] encryptedKey = copyOfRange(requestData, 0, keyDemiliterIndex);
		byte[] decryptedSymmetricKey = null;
		try {
			encryptedData = copyOfRange(requestData, keyDemiliterIndex + keySplitterLength, cipherKeyandDataLength);

			if (isThumbprint) {
				encryptedSymmetricKey = Arrays.copyOfRange(encryptedKey, THUMBPRINT_LENGTH, encryptedKey.length);
				decryptedSymmetricKey = asymmetricDecrypt(privateKey.getPrivateKey(),
						((RSAPrivateKey) privateKey.getPrivateKey()).getModulus(), encryptedSymmetricKey);
			} else {
				decryptedSymmetricKey = asymmetricDecrypt(privateKey.getPrivateKey(),
						((RSAPrivateKey) privateKey.getPrivateKey()).getModulus(),
						encryptedKey);
			}

			symmetricKey = new SecretKeySpec(decryptedSymmetricKey, 0, decryptedSymmetricKey.length, "AES");
			return symmetricDecrypt(symmetricKey, encryptedData, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new Exception("Not able to decrypt the data.");
	}

	private static int getSplitterIndex(byte[] encryptedData, int keyDemiliterIndex, String keySplitter) {
		final byte keySplitterFirstByte = keySplitter.getBytes()[0];
		final int keySplitterLength = keySplitter.length();
		for (byte data : encryptedData) {
			if (data == keySplitterFirstByte) {
				final String keySplit = new String(
						copyOfRange(encryptedData, keyDemiliterIndex, keyDemiliterIndex + keySplitterLength));
				if (keySplitter.equals(keySplit)) {
					break;
				}
			}
			keyDemiliterIndex++;
		}
		return keyDemiliterIndex;
	}

	/**
	 * 
	 * @param privateKey
	 * @param keyModulus
	 * @param data
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	private static byte[] asymmetricDecrypt(PrivateKey privateKey, BigInteger keyModulus, byte[] data)
			throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, InvalidKeyException {

		Cipher cipher;
		try {
			cipher = Cipher.getInstance(RSA_ECB_OAEP_PADDING);
			OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
					PSpecified.DEFAULT);
			cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException(e);
		} catch (NoSuchPaddingException e) {
			throw new NoSuchPaddingException(e.getMessage());
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new InvalidAlgorithmParameterException(e);
		}
	}

	/**
	 *
	 * @param paddedPlainText
	 * @param privateKey
	 * @return
	 * @throws InvalidCipherTextException
	 * @throws InvalidKeyException
	 */
	private static byte[] unpadOAEPPadding(byte[] paddedPlainText, BigInteger keyModulus)
			throws InvalidCipherTextException {

		OAEPEncoding encode = new OAEPEncoding(new RSAEngine(), new SHA256Digest());
		BigInteger exponent = new BigInteger("1");
		RSAKeyParameters keyParams = new RSAKeyParameters(false, keyModulus, exponent);
		encode.init(false, keyParams);
		return encode.processBlock(paddedPlainText, 0, paddedPlainText.length);
	}

	private static byte[] symmetricDecrypt(SecretKey key, byte[] data, byte[] aad) {
		byte[] output = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
			byte[] randomIV = Arrays.copyOfRange(data, data.length - cipher.getBlockSize(), data.length);
			SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, randomIV);

			cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
			if (aad != null && aad.length != 0) {
				cipher.updateAAD(aad);
			}
			output = cipher.doFinal(Arrays.copyOf(data, data.length - cipher.getBlockSize()));
		} catch (Exception e) {

		}
		return output;
	}

	public static byte[] getCertificateThumbprint(Certificate cert) {
		try {
			return DigestUtils.sha256(cert.getEncoded());
		} catch (java.security.cert.CertificateEncodingException e) {

			throw new CryptoManagerException(PlatformErrorMessages.CERTIFICATE_THUMBPRINT_ERROR.getCode(),
					PlatformErrorMessages.CERTIFICATE_THUMBPRINT_ERROR.getMessage(), e);
		}
	}
}
