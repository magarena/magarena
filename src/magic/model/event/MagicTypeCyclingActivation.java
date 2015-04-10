package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Arrays;

public class MagicTypeCyclingActivation extends MagicCyclingActivation {

    final String type;

    public MagicTypeCyclingActivation(final MagicManaCost aCost, final String aType) {
        super(aCost, aType + "cycle");
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
