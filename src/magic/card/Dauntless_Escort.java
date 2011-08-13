package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.*;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Dauntless_Escort {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            null,
            new MagicActivationHints(MagicTiming.Pump),
            "Indestr") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Creatures " + player + " control are indestructible this turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final Collection<MagicTarget> creatures=
                game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				game.doAction(new MagicSetAbilityAction((MagicPermanent)creature,MagicAbility.Indestructible));
			}
		}
	};
}
