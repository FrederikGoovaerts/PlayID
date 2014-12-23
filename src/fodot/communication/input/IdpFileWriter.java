package fodot.communication.input;

import java.io.File;
import java.io.IOException;

import org.ggp.base.util.files.FileUtils;

import fodot.objects.file.IFodotFile;

public class IdpFileWriter {

	public static void writeToIDPFile(IFodotFile fodot, File outputFile) {
        try {
            FileUtils.writeStringToFile(outputFile, fodot.toCode());
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}
	
	public static File createIDPFileBasedOn(File otherFile) {
		String path = otherFile.getAbsolutePath();
		if (path.contains(".")) {
			path = path.substring(0, path.lastIndexOf('.'));
		}
		
		return new File(path + ".idp");
	}
	
}
