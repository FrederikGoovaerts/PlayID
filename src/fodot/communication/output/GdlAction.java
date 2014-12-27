package fodot.communication.output;

import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.IFodotTerm;

public class GdlAction {
	private int time;
	private FodotConstant player;
	private FodotConstant action;
	
	public GdlAction(int time, FodotConstant player, FodotConstant action) {
		super();
		setTime(time);
		setPlayer(player);
		//Temporal solution: ACTION SHOULD BE PREDICATE TERM!
		setAction(action);
	}

	public FodotConstant getPlayer() {
		return player;
	}

	private void setPlayer(FodotConstant player) {
		this.player = player;
	}

	public FodotConstant getAction() {
		return action;
	}

	private void setAction(FodotConstant action) {
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
		builder.append("( does ");
		builder.append(translateTerm(player).trim() + " ");
		builder.append("( " + translateTerm(action).trim() + " ) )");
		
		//This is for when action is a predicateterm
//		builder.append(" (" + action.getName());
//		for (IFodotTerm term : action.getArguments())  {
//			builder.append(" " + translateTerm(term));
//		}
//		builder.append(")");
		
		
		return builder.toString();
	}
	
	public String translateTerm(IFodotTerm constant) {
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
