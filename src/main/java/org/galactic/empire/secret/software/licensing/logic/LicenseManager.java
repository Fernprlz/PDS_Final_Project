package org.galactic.empire.secret.software.licensing.logic;

import org.galactic.empire.secret.software.licensing.iLicenseManager;
import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.data.LicenseRequest;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;
import org.galactic.empire.secret.software.licensing.io.InitialLicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseRequestHashJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseRequestJSONParser;
import org.galactic.empire.secret.software.licensing.io.LicenseHashJSONParser;
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
		// Generate an instance of the parser and the license to return
		LicenseRequestHashJSONParser myParser = new LicenseRequestHashJSONParser();
		License LicenseToDeactivate = null;
		try {
			LicenseToDeactivate = (License) myParser.Parse(LicenseFilePath);
		} catch (LMException e) {
			System.out.println("Error: Invalid input");
		}
		// Find the license to deactivate
		License licenseFound = myStore.Find(LicenseToDeactivate);
		// Change active field to inactive
		if (licenseFound == null) {
			throw new LMException("Error: License not found");
		} else {
			licenseFound.setActive(false);
		}

		// Copy data into a license that will be returned
		return licenseFound;
	}

	public boolean verifyHash(String LicenseFilePath) throws LMException {
		// Generate an instance of the parser and the license to return
		LicenseRequestHashJSONParser myParser = new LicenseRequestHashJSONParser();
		License LicenseToVerify = null;
		try {
			LicenseToVerify = (License) myParser.Parse(LicenseFilePath);
		} catch (LMException e) {
			System.out.println("Error: Invalid input");
		}
		// Generate the signature (LicenseHash)
		SHA256Hasher mySignatureGenerator = new SHA256Hasher();
		String goodHash = null;
		try {
			// Generate new hash and update it in the license (old LicenseRequest + New Days)
			goodHash = mySignatureGenerator.generateHash(LicenseToVerify.getRequestData());
		} catch (LMException e) {
			System.out.println("Error: Hash generation failed");
		}
		
		return goodHash.equals(LicenseToVerify.getSignature());	
	}

	public License RevokeLicense(String LicenseFilePath) throws LMException {
		// Generate an instance of the parser and the license object to return
		LicenseHashJSONParser myParser = new LicenseHashJSONParser(); 
		String signatureToSearch = null;
		
		if (!verifyHash(LicenseFilePath)) {
			throw new LMException("Error: Invalid LicenseHash");
		}
		// Parse the input and search the storage list.
		try {
			signatureToSearch = (String) myParser.Parse(LicenseFilePath);

		} catch (LMException e) {
			System.out.println("Error: Invalid input format");
		}
		// Search for the license 
		License LicenseToRevoke = myStore.FindLicenseBySignature(signatureToSearch); 
		// Interpret the result of the search
		if (LicenseToRevoke == null) {
			throw new LMException("Error: No license corresponding to the specified signature");
		} else {
			try {
				// Remove from the storage list
				myStore.Remove(LicenseToRevoke);
			} catch (LMException e) {
				System.out.println("Error: License removal failed");
			}
		}
		return LicenseToRevoke;
	}

	public String UpdateLicense (String LicenseFilePath, int days) throws LMException{
		String result = null;
		// Check that the days parameter is valid, throw exception if not
		if (days <= 0) {
			throw new LMException ("Error: Invalid days");
		} else {
			// Get the license
			LicenseHashJSONParser myParser = new LicenseHashJSONParser();
			String signatureToSearch = null;
			try {
				signatureToSearch = (String) myParser.Parse(LicenseFilePath);
			} catch (LMException e) {
				System.out.println("Error: Invalid input format");
			}
			// Search for the license to update
			License LicenseToUpdate = myStore.FindLicenseBySignature(signatureToSearch);
			if (LicenseToUpdate == null) {
				throw new LMException("Error: No license corresponding to the specified signature");
			} else if (LicenseToUpdate.getActive() == true){
				// Only usable for active licenses 
				SHA256Hasher mySignatureGenerator = new SHA256Hasher();

				// Update the days left until expiration
				LicenseToUpdate.updateDays(days);

				try {
					// Generate new hash and update it in the license (old LicenseRequest + New Days)
					result = mySignatureGenerator.generateHash(LicenseToUpdate.getRequestData() + ";" + LicenseToUpdate.getDays());
				} catch (LMException e) {
					System.out.println("Error: Hash generation failed");
				}
				LicenseToUpdate.setSignature(result);
				myStore.Save();
			}

			// Returns the new signature or null if the specified one doesn't exist.
			return result;
		}
	}
}

