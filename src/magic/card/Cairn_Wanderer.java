package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Cairn_Wanderer {
    private static final long CAIRN_WANDERER_FLAGS =
        MagicAbility.Flying.getMask()|
        MagicAbility.Fear.getMask()|
        MagicAbility.FirstStrike.getMask()|
        MagicAbility.DoubleStrike.getMask()|
        MagicAbility.Deathtouch.getMask()|
        MagicAbility.Haste.getMask()|
        MagicAbility.LifeLink.getMask()|
        MagicAbility.Reach.getMask()|        
        MagicAbility.Trample.getMask()|
        MagicAbility.Shroud.getMask()|
        MagicAbility.Vigilance.getMask()|
        MagicAbility.LANDWALK_FLAGS|
        MagicAbility.PROTECTION_FLAGS;

    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            long newFlags = 0;
            for (final MagicPlayer player : game.getPlayers()) {
                for (final MagicCard card : player.getGraveyard()) {
                    final MagicCardDefinition cardDefinition = card.getCardDefinition();
                    if (cardDefinition.isCreature()) {
                        newFlags|=cardDefinition.getAbilityFlags();
                    }
                }
            }
            return flags|(newFlags & CAIRN_WANDERER_FLAGS);
        }
    };
}
