package org.galactic.empire.secret.software.licensing.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class MD5Hasher {
	public String generateHash(String text) throws LMException  {
		// Generate MD5 sum for the input fields
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new LMException("Error: no such hashing algorithm.");
		}
		
		String input =  "Stardust" + "-" + text;
		
		md.update(input.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();

		// Beware the hex length. If MD5 -> 32:"%032x", but for instance, in SHA-256 it should be "%064x" 
		String hex = String.format("%32x", new BigInteger(1, digest));

		return hex;
	}
}
