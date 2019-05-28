package org.galactic.empire.secret.software.licensing.io;

import org.galactic.empire.secret.software.licensing.exceptions.LMException;

public interface iJSONParser {
	public Object Parse (String FilePath) throws LMException;
}
