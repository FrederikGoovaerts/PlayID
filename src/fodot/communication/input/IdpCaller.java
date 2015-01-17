package fodot.communication.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import fodot.exceptions.idp.IdpConnectionException;

public class IdpCaller implements IIdpCaller {

	@Override
	public String callIDP(File file) throws IOException, IdpConnectionException {
		String fileDirectory = file.getParent();
		String fileName = file.getName();

		String command = "idp " + fileName;		

		StringBuilder result = new StringBuilder();

		ProcessBuilder builder = new ProcessBuilder(
				"cmd.exe", "/c", "cd \"" + fileDirectory + "\" && " + command);
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
