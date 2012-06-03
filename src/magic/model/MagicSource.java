package magic.model;

import magic.model.event.MagicActivation;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable {
	boolean             isSpell();
	boolean             isPermanent();
	boolean             isCreature();
	boolean             hasAbility(final MagicAbility ability);	
	MagicCardDefinition getCardDefinition();
	String              getName();
	MagicPlayer         getController();
	MagicColoredType    getColoredType();
	int                 getColorFlags();
	Collection<MagicActivation> getActivations();
}
