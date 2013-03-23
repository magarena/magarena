package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Avacyn_s_Collar {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent died) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return (equipped == died &&
                    equipped.hasSubType(MagicSubType.Human)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 white Spirit creature " +
                    "token with flying onto the battlefield."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("Spirit2")));
        }
    };
}
