package org.galactic.empire.secret.software.licensing.logicTest;

import static org.junit.jupiter.api.Assertions.*;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.logic.LicenseManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenerateLicenseTest {

	private LicenseManager myLicenseManager;
	private String jsonFilesFolder;
	
	public GenerateLicenseTest () {
		jsonFilesFolder = System.getProperty("user.dir") + "/JSONFiles/GenerateLicense/";
		myLicenseManager = LicenseManager.getInstance();
	}

	@DisplayName("Correct License Generation for All")
	@Test
	void testCorrectRequestLicenseForAll() throws LMException {
		String FilePath = this.jsonFilesFolder + "CorrectGenerateLicenseForAll.json";
		String expectedHashMap = "f7c7c78c4f36c702cec6d7ef0176a3fedd944e4cbb69312dd6910e0f094dd080";
		
		String obtainedLicenseHashMap = this.myLicenseManager.GenerateLicense(FilePath, 15);
		
		assertEquals (expectedHashMap, obtainedLicenseHashMap);
	}

}
