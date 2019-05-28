package org.galactic.empire.secret.software.licensing.logicTest;

import static org.junit.jupiter.api.Assertions.*;

import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.logic.LicenseManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestLicenseTest {
	
	private LicenseManager myLicenseManager;
	private String jsonFilesFolder;
	
	public RequestLicenseTest () {
		jsonFilesFolder = System.getProperty("user.dir") + "/JSONFiles/RequestLicense/";
		myLicenseManager = LicenseManager.getInstance();
	}

	@DisplayName("Correct License Request for All")
	@Test
	void testCorrectGenerateLicenseForAll() throws LMException {
		long unixTimeDate = System.currentTimeMillis();
		LicenseRequest expectedLicenseRequest = new LicenseRequest ("DeathStar", "Lord Vader", "vader@empire.com", "station1", "All", unixTimeDate, "b8278871e90abb910fc97a091476d07c");
		String FilePath = this.jsonFilesFolder + "CorrectRequestLicenseForAll.json";
		LicenseRequest obtainedLicenseRequest = this.myLicenseManager.RequestLicense(FilePath);
		assertEquals (expectedLicenseRequest.getStationName(), obtainedLicenseRequest.getStationName());
		assertEquals (expectedLicenseRequest.getPersoninCharge(), obtainedLicenseRequest.getPersoninCharge());
		assertEquals (expectedLicenseRequest.getEmail(), obtainedLicenseRequest.getEmail());
		assertEquals (expectedLicenseRequest.getMachineName(), obtainedLicenseRequest.getMachineName());
		assertEquals (expectedLicenseRequest.getTypeofLicense(), obtainedLicenseRequest.getTypeofLicense());
		assertEquals (expectedLicenseRequest.getUUID(), obtainedLicenseRequest.getUUID());
		assertTrue ((obtainedLicenseRequest.getrequestDate()-expectedLicenseRequest.getrequestDate()<100));
	}
	
	@DisplayName("Repeated Station Name")
	@Disabled
	@Test
	void testRepeatedStationName() throws LMException {
		String FilePath = this.jsonFilesFolder + "RepeatedStationName.json";
		String expectedError = "Error: invalid syntax. Maybe not a JSON file?";
		String obtainedError = "";
		try {
			this.myLicenseManager.RequestLicense(FilePath);			
		}
		catch (LMException ex) {
			obtainedError = ex.getMessage();
		}
		assertEquals(expectedError, obtainedError);
	}
	
	@DisplayName("Wrong Station Name Label")
	@Test
	void testWrongStationNameLabel() throws LMException {
		String FilePath = this.jsonFilesFolder + "WrongStationName.json";
		String expectedError = "JSON File Format Error: Stationame is not a correct label";
		String obtainedError = "";
		try {
			this.myLicenseManager.RequestLicense(FilePath);			
		}
		catch (LMException ex) {
			obtainedError = ex.getMessage();
		}
		assertEquals(expectedError, obtainedError);
	}
	
}
