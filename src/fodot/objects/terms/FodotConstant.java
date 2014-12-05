package fodot.objects.terms;

public class FodotConstant extends FodotTerm {

	public String value;
	
	public FodotConstant(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[constant: "+getValue()+"]";
	}

}
