package org.galactic.empire.secret.software.licensing.io;

import javax.json.JsonObject;

import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class LicenseRequestHashJSONParser extends JSONParser implements iJSONParser {
	
	public LicenseRequestHashJSONParser() {
		String[] myAcceptedLabels = {"Station Name", "Person in charge", "EMail", "Machine Name", "Type of License", "Request Date", "UUID", "LicenseHash"};
		this.acceptedLabels = myAcceptedLabels;
	}
	
	public Object Parse (String FilePath) throws LMException {
		JsonObject jsonLicenseRequest = (JsonObject)super.Parse(FilePath);
		this.checkLabels(jsonLicenseRequest);
		License myLicense = null;
		String[] values = this.getJsonStringValuesfromJson(jsonLicenseRequest);
		myLicense = new License(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
		return myLicense;	
	}

}
