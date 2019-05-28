package org.galactic.empire.secret.software.licensing.data;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.utils.SHA256Hasher;

public class License {
	
	private LicenseRequest LicenseRequestData;
	private String signature;
	private int days;
	private boolean active;
	
	public License (LicenseRequest licenseRequest, int days) throws LMException {
		this.LicenseRequestData = licenseRequest;
		SHA256Hasher mySignatureGenerator = new SHA256Hasher();
		this.signature = mySignatureGenerator.generateHash(this.LicenseRequestData.getStationName() + ";" + this.LicenseRequestData.getMachineName() + ";" + this.LicenseRequestData.getPersoninCharge() + ";" + this.LicenseRequestData.getEmail() + ";" + this.LicenseRequestData.getTypeofLicense()+ ";" + this.LicenseRequestData.getUUID());
		this.days = days;
		this.active = true;
	}

	public License (String stationName, String personInCharge, String eMail, String machineName, String typeOfLicense, String licenseSignature) throws LMException {
		this.LicenseRequestData = new LicenseRequest (personInCharge, eMail, machineName, typeOfLicense);
		this.signature = this.verifySignature(licenseSignature);
		// set to active only if no exception is thrown (inside verifySignature)
	}
	
	private String verifySignature (String signature) throws LMException {
		// Format check for license format (length and hex values)
		if (signature.length() != 64 || !signature.matches("-?[0-9a-fA-F]+")) {
			throw new LMException("Error: invalid String format for license key.");	
		}
		this.active = true;
		return signature;
	}
	
	public String getStationName() {
		return this.LicenseRequestData.getStationName();
	}
	
	public String getMachineName() {
		return this.LicenseRequestData.getMachineName();
	}
	
	public String getPersonInCharge() {
		return this.LicenseRequestData.getPersoninCharge();
	}
	
	public String getEMail () {
		return this.LicenseRequestData.getEmail();
	}
	
	public String getTypeOfLicense () {
		return this.LicenseRequestData.getTypeofLicense();
	}
	
	public long getRemainingDays () {
		long finalDate = this.LicenseRequestData.getrequestDate() + this.days*87400;
		long today  = System.currentTimeMillis(); 
		return finalDate - today;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public boolean IsValid () {
		// In the method we explore conditions to refute falseness
		boolean result = false;
		// We check if it's active.
		if (active) {
			// We check for expiration
			long finalDate = this.LicenseRequestData.getrequestDate() + this.days*86400000;
			long today  = System.currentTimeMillis(); 
			long remainingDays = finalDate - today;
			if (remainingDays>=0) {
				result = true;	
			}
		} 	
		return result;
	}
	
	public void setActive(boolean value) {
		this.active = value;
	}

}
