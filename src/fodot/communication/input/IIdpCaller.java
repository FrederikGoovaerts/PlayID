package fodot.communication.input;

import java.io.File;
import java.io.IOException;

import fodot.exceptions.idp.IdpConnectionException;

public interface IIdpCaller {
	String callIDP(File file) throws IOException,IdpConnectionException;
}
