package org.galactic.empire.secret.software.licensing.logic;


import org.galactic.empire.secret.software.licensing.iLicenseManager;
import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.io.InitialLicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.store.LicensesStore;

public class LicenseManager implements iLicenseManager {

	private static LicenseManager manager;
	private LicensesStore myStore;
	
	private LicenseManager () {
		 myStore = LicensesStore.getInstance();
	}
	
	public static LicenseManager getInstance () {
		if (manager == null) {
			manager = new LicenseManager();
		}
		else {
			System.out.println("There is a LicenseManager object already created");
		}
		return manager;
	}
	
	@Override
	public LicenseManager clone () {
		try {
			throw new CloneNotSupportedException();
		}
		catch (CloneNotSupportedException ex) {
			System.out.println("License Manager Object cannot be cloned");
		}
		return null;
	}
	
	public LicenseRequest RequestLicense(String InputFile) throws LMException {
		InitialLicenseRequestJSONParser myParser = new InitialLicenseRequestJSONParser();
		LicenseRequest myLicenseRequest = (LicenseRequest) myParser.Parse(InputFile);
		return myLicenseRequest;
	}

	public String GenerateLicense(String InputFile, int days) throws LMException {
		LicenseRequestJSONParser myParser = new LicenseRequestJSONParser();
		LicenseRequest myLicenseRequest = (LicenseRequest) myParser.Parse(InputFile);
		License myGeneratedLicense = new License  (myLicenseRequest, days);
		myStore.Add(myGeneratedLicense);
		return myGeneratedLicense.getSignature();
	}
	
	public boolean VerifyLicense(String LicenseFilePath) throws LMException {
		boolean result = false;
		
		LicenseJSONParser myParser = new LicenseJSONParser();
		License licenseToVerify = (License) myParser.Parse(LicenseFilePath);
		License licenseFound = myStore.Find(licenseToVerify);
		// Found == Not Revoked AND Input was valid
		// Check that it's not expired and active
		if (licenseFound!=null){
			result = licenseFound.IsValid();
		}
		return result;
	}
	
	public License SwitchOffLicense (String LicenseFilePath) throws LMException{
		// TODO: CHECK FOR A CORRECT INPUT JSON 
		LicenseJSONParser myParser = new LicenseJSONParser(); // Use the right JSON
		License licenseToSearch = (License) myParser.Parse(LicenseFilePath);
		// Find the license to deactivate
		License licenseFound = myStore.Find(licenseToSearch);
		// Change active field to inactive
		if (licenseFound != null) {
			licenseFound.setActive(false);
		}

		// TODO: Manage exceptions
		
		// Copy data into a license that will be returned
		return licenseFound;
	}
	
	public License RevokeLicense(String LicenseFilePath) throws LMException{
		// TODO: CHECK FOR GOOD INPUT FORMAT
		LicenseJSONParser myParser = new LicenseJSONParser(); // Use the right JSON
		License LicenseToRemove = (License) myParser.Parse(LicenseFilePath);
		// TODO: REMOVE IT FROM THE LICENSE STORE
		myStore.Remove(LicenseToRemove);
		// TODO: DO SHIT WITH EXCEPTIONS
		
		// TODO: return proper shit
		return;
	}
	
	public String UpdateLicense (String LicenseFilePath) throws LMException{
		String result = null;
		
		
		
		return result;
	}
}

