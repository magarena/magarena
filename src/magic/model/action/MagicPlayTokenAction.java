package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicStatic;
import magic.model.MagicAbility;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.trigger.MagicAtEndOfCombatTrigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MagicPlayTokenAction extends MagicPutIntoPlayAction {

    private final MagicCard card;
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition, final List<? extends MagicPermanentAction> aModifications) {
        card=MagicCard.createTokenCard(cardDefinition,player);
        setModifications(aModifications);
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition) {
        this(player, cardDefinition, Collections.<MagicPermanentAction>emptyList());
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition,final MagicPermanentAction... aModifications) {
        this(player, cardDefinition, Arrays.asList(aModifications));
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj) {
        this(player, obj.getCardDefinition(), Collections.<MagicPermanentAction>emptyList());
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj, final List<? extends MagicPermanentAction> aModifications) {
        this(player, obj.getCardDefinition(), aModifications);
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj, final MagicPermanentAction... aModifications) {
        this(player, obj.getCardDefinition(), Arrays.asList(aModifications));
    }

    public MagicPlayTokenAction(final MagicCard aCard) {
        card=aCard;
    }
    
    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        return game.createPermanent(card,card.getController());
    }
}
