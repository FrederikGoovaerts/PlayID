package fodot.patterns;

import fodot.objects.file.IFodotFile;

/**
 * This interface can be used to created classes that detect patterns in a FODOT file
 * @author Thomas
 */
public interface IFodotOptimizer {
	IFodotFile improve(IFodotFile file);
}
