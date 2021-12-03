package ekycclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import io.mosip.kernel.core.util.CryptoUtil;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class CryptoHelper {

	private static final String CERTIFICATE_TYPE = "X.509";
	
	public static Certificate convertToCertificate(String certData) throws IOException, CertificateException {
		StringReader strReader = new StringReader(certData);
		PemReader pemReader = new PemReader(strReader);
		PemObject pemObject = pemReader.readPemObject();
		
		byte[] certBytes = pemObject.getContent();
		CertificateFactory certFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE);
		return certFactory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	public static byte[] getCertificateThumbprint(Certificate cert) throws CertificateEncodingException {
		return DigestUtils.sha256(cert.getEncoded());
	}
	public static String digest(byte[] hash) throws NoSuchAlgorithmException {
		return DatatypeConverter.printHexBinary(hash).toUpperCase();
	}
	public static EncryptionResponseDto kernelEncrypt(String identity, X509Certificate x509Cert) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		SecretKey secretKey = CryptoUtility.genSecKey();
	
		EncryptionResponseDto encryptionResponseDto = new EncryptionResponseDto();
		byte[] encryptedIdentityBlock = CryptoUtility.symmetricEncrypt(identity.getBytes(StandardCharsets.UTF_8),
			secretKey, null);
	encryptionResponseDto.setEncryptedIdentity(
			CryptoUtil.encodeBase64String(encryptedIdentityBlock));
	PublicKey publicKey = x509Cert.getPublicKey();
	byte[] encryptedSessionKeyByte = CryptoUtility.asymmetricEncrypt(publicKey, (secretKey.getEncoded()));
	encryptionResponseDto.setEncryptedSessionKey(
			CryptoUtil.encodeBase64String(encryptedSessionKeyByte));
	byte[] byteArr = CryptoUtility.symmetricEncrypt(
			CryptoUtility
			.digestAsPlainText(CryptoUtility.generateHash(identity.getBytes(StandardCharsets.UTF_8))).getBytes(),
			secretKey, null);
	encryptionResponseDto.setRequestHMAC(
			CryptoUtil.encodeBase64String(byteArr));
	return encryptionResponseDto;
}
}
