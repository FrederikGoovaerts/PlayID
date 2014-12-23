package fodot.communication.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class IdpCaller implements IIdpCaller {

	@Override
	public String callIDP(File file) throws IOException {
		String fileDirectory = file.getParent();
		String fileName = file.getName();
		
		String command = "idp " + fileName;		
		
		StringBuilder result = new StringBuilder();
		
		ProcessBuilder builder = new ProcessBuilder(
	            "cmd.exe", "/c", "cd \"" + fileDirectory + "\" && " + command);
	        builder.redirectErrorStream(true);
	        Process p = builder.start();
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
