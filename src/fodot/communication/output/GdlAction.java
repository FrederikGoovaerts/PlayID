package fodot.communication.output;

import java.util.Arrays;

import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlTerm;

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.IFodotTerm;

public class GdlAction {
	private IFodotGdlTranslator translator;
	private int time;
	private IFodotTypeEnumerationElement player;
	private IFodotTypeEnumerationElement action;
	
	public GdlAction(IFodotGdlTranslator translator, int time, IFodotTypeEnumerationElement player, IFodotTypeEnumerationElement action) {
		super();
		setTranslator(translator);
		setTime(time);
		setPlayer(player);
		setAction(action);
	}

	public IFodotGdlTranslator getTranslator() {
		return translator;
	}

	public void setTranslator(IFodotGdlTranslator translator) {
		this.translator = translator;
	}

	public IFodotTypeEnumerationElement getPlayer() {
		return player;
	}

	private void setPlayer(IFodotTypeEnumerationElement player) {
		this.player = player;
	}

	public IFodotTypeEnumerationElement getAction() {
		return action;
	}

	private void setAction(IFodotTypeEnumerationElement action) {
		this.action = action;
	}

	public int getTime() {
		return time;
	}

	private void setTime(int time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return toCode();
	}
	
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		
		//TODO: action moet een predicateterm zijn, geen constant!
//		GdlTerm player = getTranslator().translate(getPlayer());
//		GdlTerm action = getTranslator().translate(getAction());
//		GdlRelation does = GdlPool.getRelation( GdlPool.getConstant("does"), Arrays.asList(player, action));
//		
//		return does.toString();
		
		builder.append("( does ");
		builder.append(translateTerm(player).trim() + " ");
		builder.append("( " + translateTerm(action).trim() + " ) )");
		return builder.toString();
	}
	
	public String translateTerm(IFodotTypeEnumerationElement constant) {
		if (constant == null) {
			return null;
		}
		//TODO: link met vertaler van GDL->FO(.)

		
		//Temporal cheaty way of translating:
		return constant.toCode().trim()
				//Replace all typical action stuff by spaces
				.replaceAll("[(]|[)]", " ")
				.replaceAll("[,]|[,][\b]", "")
				//Replace the typical c_ and p_ by nothing to translate our conventions
				.replaceAll("[a-zA-Z][_]", "");
	}
	
}
