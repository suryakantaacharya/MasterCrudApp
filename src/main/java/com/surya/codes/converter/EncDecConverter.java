/**
 *  @author Suryakanta Acharya
 *  
 *  Util class to encrypt your sensitive data while storing to db. 
 *  
 *  Pass your RSA public and private key base64 encoded strings in vm arguments.
 *  
 *  Use  @Convert(converter = EncDecConverter.class) over your entity class variable which contain sensitive data.
 * 
 *  If you need help for converting your Private and Public key to base64 encoded strings then refer : https://github.com/suryakantaacharya/Base64Util
 *  
 *  -Dpublic.key = //your public base64 encoded String
 *  -Dprivate.key = //your private base64 encoded String
 */
package com.surya.codes.converter;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncDecConverter implements AttributeConverter<String, String> {

	private static final String ENCRYPTION_ALGORITHM = "RSA";
	private static final String PRIVATE_KEY_STRING = System.getProperty("public.key");
	private static final String PUBLIC_KEY_STRING = System.getProperty("private.key");

	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			PrivateKey privateKey = getPrivateKeyFromString(PRIVATE_KEY_STRING);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptedBytes = cipher.doFinal(attribute.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error encrypting data", e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			PublicKey publicKey = getPublicKeyFromString(PUBLIC_KEY_STRING);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
			return new String(decryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error decrypting data", e);
		}
	}

	private PublicKey getPublicKeyFromString(String key) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		return keyFactory.generatePublic(spec);
	}

	private PrivateKey getPrivateKeyFromString(String key) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		return keyFactory.generatePrivate(spec);
	}
}