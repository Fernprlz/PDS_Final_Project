package org.galactic.empire.secret.software.licensing.io;

import javax.json.JsonObject;

import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class LicenseHashJSONParser extends JSONParser implements iJSONParser {
	
	public LicenseHashJSONParser () {
		String[] myAcceptedLabels = {"LicenseHash"};
		this.acceptedLabels = myAcceptedLabels;
	}
	
	public String Parse (String FilePath) throws LMException {
		JsonObject jsonLicenseRequest = (JsonObject)super.Parse(FilePath);
		this.checkLabels(jsonLicenseRequest);
		String[] signature = this.getJsonStringValuesfromJson(jsonLicenseRequest);
		
		return signature[0];	
	}

}
