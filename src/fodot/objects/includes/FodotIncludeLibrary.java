package fodot.objects.includes;


public class FodotIncludeLibrary extends FodotInclude {
	
	public static final FodotIncludeLibrary LTC = new FodotIncludeLibrary("LTC");
	
	public FodotIncludeLibrary(String libraryName) {
		super(libraryName);
	}

	@Override
	public String toCode() {
		return "include < " + getToInclude() + " >";
	}

	@Override
	public String toString() {
		return "FodotIncludeLibrary [library=" + getToInclude() + "]";
	}
		

}
