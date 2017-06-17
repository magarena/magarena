package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.SetKickerAction;
import magic.model.choice.MagicKickerChoice;

public class MagicMultikickerCost extends MagicAdditionalCost implements MagicEventAction {

    final MagicManaCost manaCost;
    final String name;

    private MagicMultikickerCost(final MagicManaCost aManaCost, final String aName) {
        manaCost = aManaCost;
        name = aName;
    }

    public MagicMultikickerCost(final MagicManaCost aManaCost) {
        this(aManaCost, "kicker");
    }

    public static MagicMultikickerCost Replicate(final MagicManaCost aManaCost) {
        return new MagicMultikickerCost(aManaCost, "replicate");
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicKickerChoice(manaCost, name),
            this,
            "PN may pay " + manaCost.getText() + " any number of times$$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.getKickerFromChosen() > 0) {
            game.doAction(new SetKickerAction(event.getKickerFromChosen()));
        }
    }
}
