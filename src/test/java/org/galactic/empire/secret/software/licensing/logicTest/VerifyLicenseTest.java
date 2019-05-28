package org.galactic.empire.secret.software.licensing.logicTest;

import static org.junit.jupiter.api.Assertions.*;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.logic.LicenseManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VerifyLicenseTest {

	private LicenseManager myLicenseManager;
	private String jsonFilesFolder;
	
	public VerifyLicenseTest () {
		jsonFilesFolder = System.getProperty("user.dir") + "/JSONFiles/VerifyLicense/";
		myLicenseManager = LicenseManager.getInstance();
	}

	@DisplayName("License Verification Correctly Evaluated as True")
	@Test
	void testCorrectVerifyLicenseFalse() throws LMException {
		boolean expectedResult = false;
		String FilePath = this.jsonFilesFolder + "CorrectVerifyLicenseTrue.json";
		
		boolean valid = this.myLicenseManager.VerifyLicense(FilePath);
		
		assertEquals (expectedResult, valid);
	}

}
