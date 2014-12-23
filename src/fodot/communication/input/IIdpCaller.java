package fodot.communication.input;

import java.io.File;
import java.io.IOException;

public interface IIdpCaller {
	String callIDP(File file) throws IOException;
}
