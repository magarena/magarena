package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicAddStaticAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenBlocksTrigger;

import java.util.Set;

public class Elder_Land_Wurm {
   private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            flags.remove(MagicAbility.Defender);
        }
   };
   
   public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
       @Override
       public MagicEvent executeTrigger(
               final MagicGame game,
               final MagicPermanent permanent,
               final MagicPermanent blocker) {
           return (permanent == blocker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN loses defender."
                ):
                MagicEvent.NONE;
       }
       @Override
       public void executeEvent(
               final MagicGame game,
               final MagicEvent event,
               final Object[] choiceResults) {
           game.doAction(new MagicAddStaticAction(event.getPermanent(), AB));
       }
   };
}
