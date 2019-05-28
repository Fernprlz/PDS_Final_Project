package org.galactic.empire.secret.software.licensing.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public abstract class JSONParser implements iJSONParser{
	
	protected String[] acceptedLabels;
	
	public Object Parse (String FilePath) throws LMException {
		String fileContents = readFile(FilePath);

		JsonObject jsonLicenseRequest = convertFromStringToJSON(fileContents);
		return jsonLicenseRequest;
	}
	
	private String readFile(String path) throws LMException {
		String fileContents = "";

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new LMException("Error: input file not found.");
		}
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				fileContents += line;
			}
		} catch (IOException e) {
			throw new LMException("Error: input file could not be accessed.");
		}
		try {
			reader.close();
		} catch (IOException e) {
			throw new LMException("Error: input file could not be closed.");
		}
		return fileContents;
	}
	
	private JsonObject convertFromStringToJSON(String fileContents) throws LMException {
		// Transform the String with the file contents into a JSON object (in memory).
		JsonObject jsonLicenseRequest = null;
		try(StringReader sr = new StringReader(fileContents)) {
			jsonLicenseRequest = Json.createReader(sr).readObject();
		} catch(JsonParsingException e) {
			throw new LMException("Error: invalid syntax. Maybe not a JSON file?");
		}
		return jsonLicenseRequest;
	}
	
	private boolean isInAcceptedLabels (String text) {
	      for (String label : this.acceptedLabels) {
	          if (text.equals(label)) {
	              return true;
	          }
	      }
		return false;
	}
	
	protected void checkLabels (JsonObject jsonLicenseRequest) throws LMException {
		for (Object keyObj: jsonLicenseRequest.keySet())
		{
		    String key = (String)keyObj;
		    if (!this.isInAcceptedLabels(key)) {
		    	throw new LMException("JSON File Format Error: " + key + " is not a correct label");
		    }
		}
	}
	
	protected String [] getJsonStringValuesfromJson (JsonObject jsonLicenseRequest) throws LMException{
		String[] values = new String[acceptedLabels.length];
		try {
			for (int i=0; i<acceptedLabels.length; i++) {
				values [i] = jsonLicenseRequest.getString(this.acceptedLabels[i]);			
			}
			return values;			
		}
		catch(JsonParsingException e) {
			throw new LMException("Error: Invalild Label");
		}		
	}
	
}
