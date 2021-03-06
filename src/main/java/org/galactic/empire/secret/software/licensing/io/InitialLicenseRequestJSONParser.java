package org.galactic.empire.secret.software.licensing.io;

import javax.json.JsonObject;
import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class InitialLicenseRequestJSONParser extends JSONParser implements iJSONParser {
	
	public InitialLicenseRequestJSONParser () {
		String[] myAcceptedLabels = {"Station Name", "Person in charge", "EMail", "Machine Name", "Type of License"};
		this.acceptedLabels = myAcceptedLabels;
	}
	
	public Object Parse (String FilePath) throws LMException {
		JsonObject jsonLicenseRequest = (JsonObject)super.Parse(FilePath);
		LicenseRequest myLicenseRequest = null;
		this.checkLabels(jsonLicenseRequest);
		String[] values = this.getJsonStringValuesfromJson(jsonLicenseRequest);
		myLicenseRequest = new LicenseRequest(values[0], values[1], values[2], values[3], values[4]);
		return myLicenseRequest;
	}	
}
