package org.galactic.empire.secret.software.licensing.logic;


import org.galactic.empire.secret.software.licensing.iLicenseManager;
import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.io.InitialLicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.store.LicensesStore;
import org.galactic.empire.secret.software.licensing.utils.SHA256Hasher;

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
		myStore.Remove(LicenseToRemove); //throws lmexception creo
		// TODO: DO SHIT WITH EXCEPTIONS
		
		// TODO: return proper shit
		return null;
	}
	
	
	public String UpdateLicense (String LicenseFilePath, int days) throws LMException{
		String result = null;
		// TODO: check that the days parameter is valid, throw exception if not
		
		// Extends the validity of a license
		// Get the license
		LicenseJSONParser myParser = new LicenseJSONParser();
		License LicenseToUpdate = (License) myParser.Parse(LicenseFilePath);
		myStore.Find(LicenseToUpdate);
		if (LicenseToUpdate != null) {
			SHA256Hasher mySignatureGenerator = new SHA256Hasher();
			
			// change the expiration date
			LicenseToUpdate.updateDays(days);
			// check and change status if needed
			if (LicenseToUpdate.getRemainingDays() <= 0) {
				LicenseToUpdate.setActive(false);
			} else {
				LicenseToUpdate.setActive(true);
			}
			// generate new hash and update it (old LicenseRequest + New Days)
			/**TODO: this means that the request date is updated to old request + literally, the new days it's active?? i suppose so - semantics**/
			result = mySignatureGenerator.generateHash(LicenseToUpdate.getRequestData() + ";" + LicenseToUpdate.getDays());
			// put the signature in the license
			LicenseToUpdate.setSignature(result);
			// TODO: create a JSON with the data
			// uses json methods like STRINGtoJSON or something
			
		}

		
		// ??? Generates a text file with the data of the new license??? -> JSON??

		// Returns the new good hash/signature
		return result;
	}
}

