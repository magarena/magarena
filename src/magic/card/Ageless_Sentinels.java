package magic.card;

import java.util.EnumSet;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Ageless_Sentinels {
	private static final MagicStatic ST = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
            flags.remove(MagicSubType.Wall);
            flags.add(MagicSubType.Bird);
            flags.add(MagicSubType.Giant);
        }
   };
   
   private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(
				final MagicGame game,
				final MagicPermanent permanent,
				final long flags) {
			return flags & ~MagicAbility.Defender.getMask();
		}
   };
   
   public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
	   @Override
	   public MagicEvent executeTrigger(
			   final MagicGame game,
			   final MagicPermanent permanent,
			   final MagicPermanent data) {
		   return (permanent == data) ?
				new MagicEvent(
					   permanent,
					   permanent.getController(),
					   new Object[]{permanent},
					   this,
					   permanent + " becomes a Bird Giant and loses defender."):
				MagicEvent.NONE;
	   }
	   @Override
	   public void executeEvent(
			   final MagicGame game,
			   final MagicEvent event,
			   final Object data[],
			   final Object[] choiceResults) {
		   final MagicPermanent permanent = (MagicPermanent)data[0];
		   game.doAction(new MagicAddStaticAction(permanent, ST));
		   game.doAction(new MagicAddStaticAction(permanent, AB));
	   }
   };
}
