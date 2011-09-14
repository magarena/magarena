package magic.model.mstatic;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicLayer;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTarget;

public abstract class MagicStatic extends MagicDummyLocalVariable {

    //permanents affected by the static effect
    protected MagicTargetFilter filter;

    //layer where this effect operate
	private final MagicLayer layer;

    //card providing the effect
	private int cardIndex;

    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter aFilter) {
        filter = aFilter;
        layer = aLayer;
	}
	
	protected MagicStatic(final MagicTargetFilter aFilter) {
		this(MagicLayer.Layer1, aFilter);
	}

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }
	
	final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
		
	public final MagicLayer getLayer() {
		return layer;
	}

    public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        return filter.accept(game, source.getController(), target);
    }
}
