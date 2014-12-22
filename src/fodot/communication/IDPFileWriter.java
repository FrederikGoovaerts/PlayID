package fodot.communication;

import java.io.File;
import java.io.IOException;

import org.ggp.base.util.files.FileUtils;

import fodot.objects.file.IFodotFile;

public class IDPFileWriter {

	public static File writeToIDPFile(IFodotFile fodot, File outputFile) {
        try {
            FileUtils.writeStringToFile(outputFile, fodot.toCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;		
	}
	
	public static File createIDPFile(String directory, String name) {
        return new File(addBackslash(directory), name + ".idp");
	}
	
	public static File createIDPFileBasedOn(File otherFile) {
		//Determine directory
		String directory = addBackslash(otherFile.getParent());
		
		//Determine name
		String name = otherFile.getName();
		if (name.contains(".")) {
			name = otherFile.getName().substring(0, name.lastIndexOf('.'));
		}
		
		return new File(directory + name + ".idp");
	}
	
	private static String addBackslash(String directory) {
		char lastChar = directory.charAt(directory.length()-1);
		if (lastChar == '\\' || lastChar == '/') {
			return directory;
		}
		return directory + '\\';
	}
	
}
