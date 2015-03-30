package playid.domain.communication.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import playid.domain.exceptions.idp.IdpConnectionException;
import playid.util.OSUtil;

public class IdpCaller implements IIdpCaller {

	private boolean displayWarnings;
	
	public IdpCaller(boolean displayWarnings) {
		this.displayWarnings = displayWarnings;
	}
	
	@Override
	public String callIDP(File file) throws IOException, IdpConnectionException {
		String fileDirectory = file.getParent();
		String fileName = file.getName();

		String command = "idp";

		if (!displayWarnings) {
			command = command + " --nowarnings";
		}
		
		
		StringBuilder result = new StringBuilder();

		ProcessBuilder builder ;
        if( OSUtil.isWindows()){
            builder = new ProcessBuilder(
				"cmd.exe", "/c", "cd \"" + fileDirectory + "\" && " + command + " " + fileName);
        } else {
            builder = new ProcessBuilder(
                    "idp", fileDirectory + "/" + fileName);
        }


		builder.redirectErrorStream(true);

		Process p = null;
		try {
			p = builder.start();
		} catch (IOException e) {
			throw new IdpConnectionException(e.getMessage());
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) { break; }
			result.append(line + "\n");
		}

		return result.toString();
	}
}
