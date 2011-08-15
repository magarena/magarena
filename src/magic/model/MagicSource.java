package magic.model;

import magic.model.event.MagicActivation;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable {
	public boolean             isSpell();
	public boolean             isPermanent();
	public boolean             isCreature();
	public boolean             hasAbility(final MagicGame game,final MagicAbility ability);	
	public MagicCardDefinition getCardDefinition();
	public String              getName();
	public MagicPlayer         getController();
	public MagicColoredType    getColoredType();
	public int                 getColorFlags();
	public Collection<MagicActivation> getActivations();
}
