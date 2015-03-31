package playid.domain.communication.output;

import java.util.Arrays;

import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlTerm;

import playid.domain.communication.gdloutput.IFodotGdlTranslator;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;

@Deprecated
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
	
	public String toCode() {//		
		GdlTerm gdlPlayer = getTranslator().translate(getPlayer());
		GdlTerm gdlAction = getTranslator().translate(getAction());
		GdlRelation does = GdlPool.getRelation( GdlPool.getConstant("does"), Arrays.asList(gdlPlayer, gdlAction));
		return does.toString();
	}	
}
