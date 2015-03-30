package playid.domain.communication.input;

import java.io.File;
import java.io.IOException;

import playid.domain.exceptions.idp.IdpConnectionException;

public interface IIdpCaller {
	String callIDP(File file) throws IOException,IdpConnectionException;
}
