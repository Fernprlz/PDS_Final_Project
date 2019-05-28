package org.galactic.empire.secret.software.licensing.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class SHA256Hasher {
	public String generateHash (String dataToSign) throws LMException  {
		// Generate SHA-256 sum for the input fields -> License key
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new LMException("Error: no such hashing algorithm.");
		}

		md.update(dataToSign.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();

		// Beware the hex length. If MD5 -> 32:"%032x", but for instance, in SHA-256 it should be "%064x"
		String signature = String.format("%064x", new BigInteger(1, digest));

		return signature;
	}
}
