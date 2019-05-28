package org.galactic.empire.secret.software.licensing.store;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.galactic.empire.secret.software.licensing.data.License;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public class LicensesStore {
	
	private static LicensesStore instance;
	private List<License> licensesList;
		
	private LicensesStore () {
		licensesList = null;
	}
	
	public static LicensesStore getInstance () {
		if (instance == null) {
			instance = new LicensesStore();
		}
		else {
			System.out.println("There is a LicenseStore object already created");
		}
		return instance;
	}
	
	@Override
	public LicensesStore clone () {
		try {
			throw new CloneNotSupportedException();
		}
		catch (CloneNotSupportedException ex) {
			System.out.println("LicenseStore Object cannot be cloned");
		}
		return null;
	}
	
	private void Load () {
		try
		{
			JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir") + "/Store/licensesStore.json"));
			Gson gson = new Gson();
			License [] myArray = gson.fromJson(reader, License[].class);
			this.licensesList = new ArrayList<License>();
			for (License lic: myArray) {
				this.licensesList.add(lic);
			}
		}
		catch (Exception ex)
		{		
		}	
	}
	
	public void Add (License newLicense) throws LMException {
		this.Load();
		if (Find(newLicense)==null) {
			licensesList.add(newLicense);
			this.Save();
		}
	}
	
	public void Remove (License LicenseToRemove) throws LMException{
		this.Load();
		if (Find(LicenseToRemove) != null) {
			licensesList.remove(LicenseToRemove);
			this.Save();
		}
	}
	
	private void Save () throws LMException {
		Gson gson = new Gson();  
		String jsonString = gson.toJson(this.licensesList);
        FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(System.getProperty("user.dir") + "/Store/licensesStore.json");
	        fileWriter.write(jsonString);
	        fileWriter.close();
		} catch (IOException e) {
			throw new LMException("Error: Unable to save a new license in the internal licenses store");
		}
	}
	
	public License Find (License licenseToFind) {
		License result = null;
		this.Load();
	    for (License lic : this.licensesList) {
	        if (lic.getSignature().equals(licenseToFind.getSignature())) {
	        	if ((lic.getStationName().equals(licenseToFind.getStationName())) && (lic.getMachineName().equals(licenseToFind.getMachineName())) && (lic.getPersonInCharge().equals(licenseToFind.getPersonInCharge())) && (lic.getEMail().equals(licenseToFind.getEMail()))) {
	        		result = lic;
	        	}
	        }
	    }
		return result;
	}

}
