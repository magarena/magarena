package magic.model.stack;

import javax.swing.ImageIcon;

import magic.model.MagicAbility;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicPlayer;

public class MagicCardOnStack extends MagicItemOnStack {

	private MagicLocationType moveLocation=MagicLocationType.Graveyard;
	
	public MagicCardOnStack(final MagicCard card,final MagicPlayer controller,final MagicPayedCost payedCost) {

		setSource(card);
		setController(controller);
		setEvent(card.getCardDefinition().getCardEvent().getEvent(this,payedCost));
	}
	
	public MagicCardOnStack(final MagicCard card,final MagicPayedCost payedCost) {
		
		this(card,card.getController(),payedCost);
	}
	
	private MagicCardOnStack() {
		
	}
	
	@Override
	public MagicCopyable create() {
		
		return new MagicCardOnStack();
	}
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {

		super.copy(copyMap,source);
		moveLocation=((MagicCardOnStack)source).moveLocation;
	}
	
	public MagicCard getCard() {
		
		return (MagicCard)getSource();
	}
	
	public MagicCardDefinition getCardDefinition() {
		
		return getCard().getCardDefinition();
	}

	public void setMoveLocation(final MagicLocationType moveLocation) {
		
		this.moveLocation=moveLocation;
	}
	
	public MagicLocationType getMoveLocation() {
		
		return moveLocation;
	}

	@Override
	public boolean isSpell() {

		return true;
	}
	
	@Override
	public boolean canBeCountered() {
		
		return !getCardDefinition().hasAbility(MagicAbility.CannotBeCountered);
	}
	
	@Override
	public ImageIcon getIcon() {
		
		return getCard().getCardDefinition().getIcon();
	}
}