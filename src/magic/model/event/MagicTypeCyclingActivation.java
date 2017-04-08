package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicTargetChoice;

public class MagicTypeCyclingActivation extends MagicCyclingActivation {

    final String type;

    public MagicTypeCyclingActivation(final MagicMatchedCostEvent aMatchedCost, final String aType) {
        super(aMatchedCost, aType + "cycle");
        type = aType;
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
