package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicLocationType;
import magic.model.choice.MagicTargetChoice;

import java.util.Arrays;

public class MagicTypeCyclingActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;
    final String type;

    public MagicTypeCyclingActivation(final MagicManaCost aCost, final String aType) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            aType + "cycle"
        );
        cost = aCost;
        type = aType;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicDiscardSelfEvent(source)
        );
    }
    
    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return new MagicEvent(
            card,
            this,
            "PN searches his or her library for a " + type + " card. " +
            "Then PN shuffles his or her library."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.addEvent(new MagicSearchToLocationEvent(
            event, 
            new MagicTargetChoice("a "+type+" card from your library"),
            MagicLocationType.OwnersHand
        ));
    }
}
