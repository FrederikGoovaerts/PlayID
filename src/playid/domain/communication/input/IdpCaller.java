package playid.domain.communication.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import playid.domain.communication.output.IdpResultTransformer;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.structure.FodotStructure;
import playid.util.OSUtil;

public class IdpCaller {

	private boolean displayWarnings;

	public IdpCaller(boolean displayWarnings) {
		this.displayWarnings = displayWarnings;
	}

	public String callIdp(File file) throws IOException, IdpConnectionException {

		List<String> commandChain = new ArrayList<String>();
        ProcessBuilder builder;
        StringBuilder idpCommand = new StringBuilder();

		if (OSUtil.isWindows()) {
            // Start commandline in windows
			commandChain.addAll(Arrays.asList("cmd.exe", "/c"));
            // Form idp command prefix
            idpCommand.append("idp ");
            if (!displayWarnings) {
                idpCommand.append("--nowarnings ");
            }
		} else {
            // Add idp command and parameters to the commandchain
            commandChain.add("idp");
            if (!displayWarnings) {
                commandChain.add("--nowarnings");
            }
        }

		idpCommand.append(file.getAbsolutePath());
		
		//Add idp command/filename to commandchain
		commandChain.add(idpCommand.toString());

		// Create process builder
        if (OSUtil.isWindows()) {
            builder = new ProcessBuilder(commandChain);
        } else {
            builder = new ProcessBuilder(commandChain);
        }
		builder.redirectErrorStream(true); // TODO dit op false?

		//Start process
		Process p = null;
		try {
			p = builder.start();
		} catch (IOException e) {
			throw new IdpConnectionException(e.getMessage());
		}

		//Parse
		StringBuilder result = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			result.append(line + "\n");
		}

		return result.toString();
	}

	public List<FodotStructure> generateModels(IFodotFile fodotFile,
			File outputFile) throws IdpConnectionException, IOException {
		// Create IDPfile in same location as GDL file
		IdpFileWriter.writeToIDPFile(fodotFile, outputFile);

		// Make IDP solve it
		String idpResult = callIdp(outputFile);

		// TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
		idpResult = fixResult(idpResult);

		IdpResultTransformer resultTransformer = new IdpResultTransformer(
				fodotFile, idpResult);
		List<FodotStructure> models = resultTransformer.getModels();

		return models;
	}

	public List<FodotStructure> generateModels(IFodotFile fodotFile)
			throws IOException {
		File tempFile = File.createTempFile("playid", ".idp").getAbsoluteFile();
		return generateModels(fodotFile, tempFile);
	}

	private String fixResult(String idpResult) {
		String stupidWarning = "Warning: XSB support is not available. Option xsb is ignored.\n\n";
		if (idpResult.contains(stupidWarning)) {
			idpResult = idpResult.replaceAll(stupidWarning, "");
		}
		return idpResult;
	}
}
