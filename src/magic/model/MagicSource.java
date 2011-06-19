package magic.model;

import java.util.Collection;

import magic.model.event.MagicActivation;

public interface MagicSource extends MagicCopyable, MagicMappable {

	public MagicCardDefinition getCardDefinition();
	
	public String getName();
		
	public boolean isSpell();
	
	public boolean isPermanent();
	
	public MagicPlayer getController();
	
	public MagicColoredType getColoredType();
	
	public int getColorFlags();
	
	public boolean hasAbility(final MagicGame game,final MagicAbility ability);	
	
	public Collection<MagicActivation> getActivations();

    public long getId();
}
