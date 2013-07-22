package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicThiefTrigger.Type;
import magic.model.trigger.MagicThiefTrigger.Player;

public class MagicSpecterTrigger extends MagicWhenDamageIsDealtTrigger {

    private final Type type;
    private final Player player;
    private final boolean random;
    private int amt;
    
    public MagicSpecterTrigger(final Type type,final Player player) {
        this(type, player, false, 1);
    }
    
    public MagicSpecterTrigger(final Type type,final Player player, final int amt) {
        this(type, player, false, amt);
    }

    public static MagicSpecterTrigger Random(final Type type,final Player player) {
        return new MagicSpecterTrigger(type, player, true, 1);
    }
    
    public static MagicSpecterTrigger Random(final Type type,final Player player, final int amt) {
        return new MagicSpecterTrigger(type, player, true, amt);
    }
    
    private MagicSpecterTrigger(final Type aType,final Player aPlayer,final boolean aRandom, final int aAmt) {
        type = aType;
        player = aPlayer;
        random = aRandom;
        amt = aAmt;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        final MagicTarget target = damage.getTarget();
        return (damage.getSource() == permanent &&
                target.isPlayer() &&
                ((MagicPlayer)target).getHandSize() > 0 &&
                (player == Player.Any || permanent.isOpponent(target)) &&
                (type == Type.Any || damage.isCombat())) ?
            new MagicEvent(
                permanent,
                (MagicPlayer)target,
                this,
                amt == 1 ?
                    "PN discards a card" + (random ? " at random." : ".") :
                    "PN discards " + amt + " cards" + (random ? " at random." : ".")
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.addEvent(new MagicDiscardEvent(
            event.getSource(),
            event.getPlayer(),
            amt,
            random
        ));
    }
}
