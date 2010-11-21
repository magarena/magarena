package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;

public class MagicPlayChoiceResult implements MagicMappable {

	public static final MagicPlayChoiceResult PASS=new MagicPlayChoiceResult(null,null);
	
	public MagicSource source;
	public MagicActivation activation;
	
	public MagicPlayChoiceResult(final MagicSource source,final MagicActivation activation) {
		
		this.source=source;
		this.activation=activation;
	}
	
	@Override
	public Object map(final MagicGame game) {

		if (this==PASS) {
			return PASS;
		}
		return new MagicPlayChoiceResult((MagicSource)source.map(game),activation);
	}
	
	@Override
	public String toString() {
		
		if (this==PASS) {
			return "pass";
		} else {
			return source.getName();
		}
	}
}