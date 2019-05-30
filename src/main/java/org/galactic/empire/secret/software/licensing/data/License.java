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
	
	public License (String stationName, String personInCharge, String eMail, String machineName, String typeOfLicense, String UUID, String licenseSignature) throws LMException {
		this.LicenseRequestData = new LicenseRequest (personInCharge, eMail, machineName, typeOfLicense);
		this.signature = this.verifySignature(licenseSignature);
		// set to active only if no exception is thrown (inside verifySignature)
	}
	
	/**
	 * 
	 * @param stationName
	 * @param personInCharge
	 * @param eMail
	 * @param machineName
	 * @param typeOfLicense
	 * @param UUID
	 * @param licenseSignature
	 * @throws LMException
	 */
	public License (String stationName, String personInCharge, String eMail, String machineName, String typeOfLicense, String RequestDate, String UUID, String licenseSignature) throws LMException {
		this.LicenseRequestData = new LicenseRequest (stationName, personInCharge, eMail, machineName, typeOfLicense, Long.parseLong(RequestDate), UUID);
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
	
	/**
	 * Getter for the new added field: valid
	 * @return
	 */
	public boolean getActive() {
		return active;
	}
	
	/**
	 * Getter for the field 'days';
	 */
	public int getDays() {
		return this.days;
	}
	
	/**
	 * Updates the days field
	 * @param newDays
	 */
	public void updateDays(int newDays) {
		days += newDays;
	}
	
	/**
	 * Modified isValid method that considers activeness
	 * @return
	 */
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
	
	/**
	 * Setter for the new added field: valid
	 * @param value
	 */
	public void setActive(boolean value) {
		this.active = value;
	}
	
	/**
	 * Setter that allows to update the 'days' field.
	 * @param newDays
	 */
	public void setDays(int newDays) {
		this.days = newDays;
	}
	
	/**
	 * Setter that allows to update the 'signature' field.
	 * @param signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	/**
	 * Getter that returns the relevant data to generate a new signature.
	 * @return
	 */
	public String getRequestData() {
		String result = LicenseRequestData.getStationName() + ";" + LicenseRequestData.getPersoninCharge() + ";" +
						LicenseRequestData.getEmail() + ";" + LicenseRequestData.getMachineName() + ";" + 
						LicenseRequestData.getTypeofLicense() + ";" + LicenseRequestData.getUUID();
		return result;
	}
	
	public String licenseDataToJSONString() {
		String result = "{\"Station Name\":\"" + LicenseRequestData.getStationName() + "\"" +
						",\"Person in charge\":\"" + LicenseRequestData.getPersoninCharge() + "\"" +
						",\"EMail\":\"" + LicenseRequestData.getEmail() + "\"" +
						",\"Machine Name\":\"" + LicenseRequestData.getMachineName() + "\"" +
						",\"Type of License\":\"" + LicenseRequestData.getTypeofLicense() + "\"" +
						",\"UUID\":\"" + LicenseRequestData.getUUID() + "\"" +
						",\"Signature\":\"" + this.signature + "\"" +
						",\"Days\":\"" + this.days + "\"" +
						"\"Active\":\"" + this.active + "\"}";
					
						
		return result;
	}

}
