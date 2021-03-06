package playid.domain.fodot.includes.elements;


public class FodotIncludeLibrary extends FodotIncludeStatement {
	
	public static final FodotIncludeLibrary LTC = new FodotIncludeLibrary("LTC");
	
	public FodotIncludeLibrary(String libraryName) {
		super(libraryName);
	}

	@Override
	public String toCode() {
		return "include <" + getToInclude() + ">";
	}

	@Override
	public String toString() {
		return "FodotIncludeLibrary [library=" + getToInclude() + "]";
	}
		

}
