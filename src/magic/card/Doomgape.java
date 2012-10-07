package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Doomgape {

    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.SACRIFICE_CREATURE,
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "Sacrifice a creature. " + 
                    "PN gains life equal to that creature's toughness."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final int toughness=creature.getToughness();
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),toughness));
                }
            });
        }
    };
}
