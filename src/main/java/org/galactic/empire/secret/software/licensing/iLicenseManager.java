package org.galactic.empire.secret.software.licensing;

import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public interface iLicenseManager {
	LicenseRequest RequestLicense (String InputFile) throws LMException;
	// LicenseRequest represents LM-RF-01-S1
	// String InputFile represents LM-RF-01-E1
	// LMException represents LM-RF-01-S2
	
	String GenerateLicense (String InputFile, int days) throws LMException;
	// String represents LM-RF-02-S1
	// String InputFile represents LM-RF-02-E1
	// int days represents LM-RF-02-E2
	// LMException represents LM -RF-02-S2
	
	boolean VerifyLicense (String LicenseFilePath) throws LMException;
	//boolean represents LM-RF-03-S1
	//String LicenseFilePath represents LM-RF-03-E1
	//LMException represents LM -RF-03-S2
	
	License SwitchOffLicense (String LicenseFilePath) throws LMException;
	// License represents the license that has been set to "inactive"
	// String LicenseFilePath represents the path to the file including the 
	// input required for the functionality
	// LMExeption represents the possible error situations
	
	License RevokeLicense (String LicenseFilePath) throws LMException;
	// License represents the license that has been revoked
	// String LicenseFilePath represents the path to the file including the 
	// input required for the functionality
	// LMException represents the possible error situations
	
	String UpdateLicense (String LicenseFilePath, int days) throws LMException;
	// String represents the upgraded hash for the license being updated.
	// String FilePathToLicenseInformation represents the path to the 
	// file including the input required for the functionality
	// int days represents the number of days increased for the license validity
	// LMException represents represents the possible error situations
	
}


