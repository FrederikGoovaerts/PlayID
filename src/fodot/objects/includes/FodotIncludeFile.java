package fodot.objects.includes;

public class FodotIncludeFile extends FodotInclude {

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
