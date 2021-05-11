package su.usatu.project26.util;

import java.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;

public class PasswordUtil {
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int ITERATIONS = 1000;
	private static final int KEY_LENGTH = 192; // bits

	public static String getSalt() {

		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);

	}

	public static String hashPassword(String password, String salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		char[] passwordChars = password.toCharArray();
		byte[] saltBytes = salt.getBytes();

		PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);
		SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hashedPassword = key.generateSecret(spec).getEncoded();
		return String.format("%x", new BigInteger(hashedPassword));

	}

	public static boolean checkPassword(String password, String salt, String expectedHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String hashToCheck = hashPassword(password, salt);

		if (hashToCheck.length() != expectedHash.length())
			return false;
		if (!hashToCheck.equals(expectedHash))
			return false;

		return true;
	}

}
