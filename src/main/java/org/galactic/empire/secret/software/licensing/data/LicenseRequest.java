package org.galactic.empire.secret.software.licensing.data;

import java.util.regex.Pattern;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.utils.MD5Hasher;


public class LicenseRequest {
	private String StationName;
	private String PersoninCharge; 
	private String Email;
	private String MachineName;
	private String TypeofLicense;
	private String UUID;
	private long  requestDateUnixTime;
	
	public LicenseRequest() {
	}

	public LicenseRequest(String StationName, String PersoninCharge, String Email, String MachineName, String TypeofLicense) throws LMException {
		this.StationName = StationName;
		this.PersoninCharge = this.verifyPersonInCharge(PersoninCharge);
		this.Email= this.verifyEmail(Email);
		this.MachineName = this.verifyMachineName(MachineName);
		this.TypeofLicense = this.verifyTypeOfLicense(TypeofLicense);
		MD5Hasher myHasher = new MD5Hasher ();
		this.UUID = myHasher.generateHash(this.StationName+ ";" + this.MachineName);
		this.requestDateUnixTime = getToday();
	}
	
	public LicenseRequest(String StationName, String PersoninCharge, String Email, String MachineName, String TypeofLicense,  long requestDateUnixTime, String UUID) throws LMException {
		this.StationName = StationName;
		this.PersoninCharge = this.verifyPersonInCharge(PersoninCharge);
		this.Email= this.verifyEmail(Email);
		this.MachineName = this.verifyMachineName(MachineName);
		this.TypeofLicense = this.verifyTypeOfLicense(TypeofLicense);
		this.UUID = this.verifyUUID(UUID);
		this.requestDateUnixTime = this.verifyRequestDate(requestDateUnixTime);
	}

	public LicenseRequest(String PersoninCharge, String Email, String MachineName, String TypeofLicense) throws LMException {
		this.PersoninCharge = this.verifyPersonInCharge(PersoninCharge);
		this.Email= this.verifyEmail(Email);
		this.MachineName = this.verifyMachineName(MachineName);
		this.TypeofLicense = this.verifyTypeOfLicense(TypeofLicense);
	}
	
	private String verifyUUID (String uuid) throws LMException {
		if (uuid.length() != 32 || !uuid.matches("-?[0-9a-fA-F]+")) {
			throw new LMException("Error: invalid String format for license key.");	
		}
		return uuid;
	}
	
	private long verifyRequestDate (long date) throws LMException {
		if (date>=1 && (date - 999999999) > 0) {
		}
		else {
			throw new LMException("Request Date is wrong");
		}
		return date;
	}
	
	private String verifyPersonInCharge (String personInCharge) throws LMException {
		if (personInCharge.length() < 1 || personInCharge.length() > 20) {
			throw new LMException("Error: invalid String length for person in charge.");	
		}
		return personInCharge;
	}
	
	private String verifyEmail (String email) throws LMException {
		Pattern mailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

		if (!mailPattern.matcher(email).matches()) {
			throw new LMException("Error: invalid E-mail data in JSON structure.");	
		}
		return email;
	}
	
	private String verifyMachineName (String machineName) throws LMException{
		if (machineName.length() < 1 || machineName.length() > 10) {
			throw new LMException("Error: invalid String length for machine name.");	
		}
		return machineName;
	}
	
	private String verifyTypeOfLicense (String typeOfLicense) throws LMException{
		if (!(typeOfLicense.equalsIgnoreCase("Fighter") || typeOfLicense.equalsIgnoreCase("Starship") || typeOfLicense.equalsIgnoreCase("All"))) {
			throw new LMException("Error: invalid type of license request.");	
		}
		return typeOfLicense;
	}
	
	private long getToday() {
	    long fechaUnixTime = System.currentTimeMillis();    // Unix timestamp in milliseconds
		return fechaUnixTime;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String StationName) {
		this.StationName = StationName;
	}

	public String getPersoninCharge() {
		return PersoninCharge;
	}

	public void setPersoninCharge(String PersoninCharge) {
		this.PersoninCharge = PersoninCharge;
	}		
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}
	public String getMachineName() {
		return MachineName;
	}

	public void setMachineName(String MachineName) {
		this.MachineName = MachineName;
	}
	public String getTypeofLicense() {
		return TypeofLicense;
	}
	public void setTypeofLicense(String TypeofLicense) {
		this.TypeofLicense = TypeofLicense;
	}

	public long getrequestDate() {
		return requestDateUnixTime;
	}

	public void setrequestDate(long requestDateUnixTime) {
		this.requestDateUnixTime = requestDateUnixTime;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
	}		
	public String getUUID() {
		return UUID;
	}

	@Override
	public String toString() {
		return StationName + PersoninCharge + Email + MachineName + 
				TypeofLicense + UUID + requestDateUnixTime+ "\n]";
	}
}
