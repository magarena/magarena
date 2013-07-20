package magic.model.event;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.action.MagicSetKickerAction;

public class MagicKickerCost extends MagicAdditionalCost implements MagicEventAction {

    final MagicManaCost manaCost;
    final String name;

    private MagicKickerCost(final MagicManaCost aManaCost, final String aName) {
        manaCost = aManaCost;
        name = aName;
    }

    public MagicKickerCost(final MagicManaCost aManaCost) {
        this(aManaCost, "kicker");
    }

    public static MagicKickerCost Buyback(final MagicManaCost aManaCost) {
        return new MagicKickerCost(aManaCost, "buyback");
    }

    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicMayChoice(
                "Pay the " + name + " " + manaCost.getText() + '?',
                new MagicPayManaCostChoice(manaCost)
            ),
            this,
            "PN may$ pay " + manaCost.getText() + "$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.payManaCost(game);
            game.doAction(new MagicSetKickerAction(1));
        }
    }
}
