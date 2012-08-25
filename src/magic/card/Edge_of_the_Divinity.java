package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Edge_of_the_Divinity {
    public static final Object S1 = new MagicStatic(MagicLayer.ModPT) {
            @Override
            public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
                pt.add(1, 2);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target) && MagicColor.White.hasColor(target.getColorFlags());
            }
        };

    public static final Object S2 = new MagicStatic(MagicLayer.ModPT) {
            @Override
            public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
                pt.add(2, 1);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target) && MagicColor.Black.hasColor(target.getColorFlags());
            }
        };
}
