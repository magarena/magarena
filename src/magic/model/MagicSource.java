package magic.model;

import magic.model.event.MagicActivation;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable {
	boolean             isSpell();
	boolean             isPermanent();
	boolean             isCreature();
	boolean             hasAbility(final MagicGame game,final MagicAbility ability);	
	MagicCardDefinition getCardDefinition();
	String              getName();
	MagicPlayer         getController();
	MagicColoredType    getColoredType();
	int                 getColorFlags(final MagicGame game);
	Collection<MagicActivation> getActivations();
}
