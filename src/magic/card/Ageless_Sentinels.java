package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenBlocksTrigger;

import java.util.Set;

public class Ageless_Sentinels {
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(
                final MagicPermanent permanent,
                final Set<MagicSubType> flags) {
            flags.remove(MagicSubType.Wall);
            flags.add(MagicSubType.Bird);
            flags.add(MagicSubType.Giant);
        }
   };
   
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
                   "SN becomes a Bird Giant and loses defender."):
                MagicEvent.NONE;
       }
       @Override
       public void executeEvent(
               final MagicGame game,
               final MagicEvent event,
               final Object[] choiceResults) {
           final MagicPermanent permanent = event.getPermanent();
           game.doAction(new MagicAddStaticAction(permanent, ST));
           game.doAction(new MagicAddStaticAction(permanent, AB));
       }
   };
}
