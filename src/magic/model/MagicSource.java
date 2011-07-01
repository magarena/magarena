package magic.model;

import java.util.Collection;

import magic.model.event.MagicActivation;

public interface MagicSource extends MagicCopyable, MagicMappable {
	public boolean             isSpell();
	public boolean             isPermanent();
	public boolean             hasAbility(final MagicGame game,final MagicAbility ability);	
	public MagicCardDefinition getCardDefinition();
	public String              getName();
	public MagicPlayer         getController();
	public MagicColoredType    getColoredType();
	public int                 getColorFlags();
	public Collection<MagicActivation> getActivations();
}
