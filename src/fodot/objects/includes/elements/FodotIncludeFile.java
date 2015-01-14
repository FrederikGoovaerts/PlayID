package fodot.objects.includes.elements;

public class FodotIncludeFile extends FodotIncludeStatement {

	public FodotIncludeFile(String path) {
		super(path);
	}

	@Override
	public String toCode() {
		return "include \"" + getToInclude() + "\"";
	}

	@Override
	public String toString() {
		return "FodotIncludeFile [file=" + getToInclude() + "]";
	}

}
