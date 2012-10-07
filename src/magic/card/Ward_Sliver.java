package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicAddStaticAction;
import magic.model.choice.MagicColorChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Ward_Sliver {
      public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color$." + 
                " All Slivers have protection from chosen color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            final MagicColor color=(MagicColor)choiceResults[0];
            final MagicAbility protection = color.getProtectionAbility();
            game.doAction(new MagicAddStaticAction((MagicPermanent)event.getSource(), new MagicStatic(
                MagicLayer.Ability, 
                MagicTargetFilter.TARGET_SLIVER) {
                    @Override
                    public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
                        return flags | protection.getMask();
                    }
                } 
            ));
        }
    };
}
