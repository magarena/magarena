package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.event.MagicRedirectDamageEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTriggerType;

public class MagicDealDamageAction extends MagicAction {

    private static final int UNINIT = Integer.MIN_VALUE;

    private final MagicDamage damage;
    private MagicTarget target;
    private int oldDamage = UNINIT;
    private int oldPrevent = UNINIT;
    
    public MagicDealDamageAction(final MagicSource source, MagicTarget target, final int amt) {
        this(new MagicDamage(source, target, amt), null);
    }

    public MagicDealDamageAction(final MagicDamage damage) {
        this(damage, null);
    }

    public static final MagicDealDamageAction NoRedirect(final MagicDamage damage) {
        return new MagicDealDamageAction(damage, damage.getTarget());
    }

    private MagicDealDamageAction(final MagicDamage aDamage, final MagicTarget aTarget) {
        damage = aDamage;
        target = aTarget;
    }

    private int preventDamage(final MagicGame game,int amount) {
        // Damage is not preventable.
        if (damage.isUnpreventable()) {
            return amount;
        }
        
        if (target.isPermanent()) {
            final MagicPermanent targetPermanent = (MagicPermanent)target;
            if (targetPermanent.hasProtectionFrom(damage.getSource())) {
                return 0;
            }
        }

        // Prevent x amount of damage.
        final int prevent=target.getPreventDamage();
        if (prevent>0) {
            final int min=Math.min(amount,prevent);
            amount-=min;
            oldPrevent=prevent;
            target.setPreventDamage(prevent-min);
        }
        return amount;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.IfDamageWouldBeDealt,damage);
        
        /*
        306.7. If damage would be dealt to a player by a source
        controlled by an opponent, that opponent may have that source deal that
        damage to a planeswalker the first player controls instead.
        */

        // in immediate mode, always deal damage to player
        if (game.isImmediate() == false &&
            target == null &&
            damage.getTarget().isPlayer() &&
            damage.getSource().getController() != damage.getTarget() &&
            damage.getTarget().getController().controlsPermanent(MagicType.Planeswalker)) {
            game.addEvent(new MagicRedirectDamageEvent(damage));
            return;
        }

        target = damage.getTarget();

        /*
        119.1. Objects can deal damage to creatures, planeswalkers, and players.
        */
        if (!target.isCreature() &&
            !target.isPlaneswalker() &&
            !target.isPlayer()) {
            return;
        }

        damage.setDealtAmount(0);
        int dealtAmount=damage.getAmount();
        if (dealtAmount<=0) {
            return;
        }

        dealtAmount=preventDamage(game,dealtAmount);
        if (dealtAmount<=0) {
            return;
        }

        final MagicSource source=damage.getSource();

        if (target.isPlaneswalker()) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            game.doAction(new ChangeCountersAction(targetPermanent,MagicCounterType.Loyalty,-dealtAmount));
        }

        if (target.isCreature()) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            if (damage.hasNoRegeneration()) {
                game.doAction(MagicChangeStateAction.Set(targetPermanent,MagicPermanentState.CannotBeRegenerated));
            }
            if (source.hasAbility(MagicAbility.Wither)||source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new ChangeCountersAction(targetPermanent,MagicCounterType.MinusOne,dealtAmount));
            } else {
                oldDamage=targetPermanent.getDamage();
                targetPermanent.setDamage(oldDamage+dealtAmount);
            }
            if (source.hasAbility(MagicAbility.Deathtouch)) {
                game.doAction(MagicChangeStateAction.Set(targetPermanent,MagicPermanentState.Destroyed));
            }
            game.doAction(MagicChangeStateAction.Set(targetPermanent,MagicPermanentState.WasDealtDamage));
        }

        if (target.isPlayer()) {
            final MagicPlayer targetPlayer = (MagicPlayer)target;
            if (source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new ChangePoisonAction(targetPlayer,dealtAmount));
            } else {
                game.doAction(new ChangeLifeAction(targetPlayer,-dealtAmount));
            }
            game.doAction(new ChangePlayerStateAction(targetPlayer,MagicPlayerState.WasDealtDamage));
        }

        damage.setDealtAmount(dealtAmount);
        if (source.hasAbility(MagicAbility.Lifelink)) {
            game.doAction(new ChangeLifeAction(source.getController(),dealtAmount));
        }
        game.executeTrigger(MagicTriggerType.WhenDamageIsDealt,damage);

        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (oldDamage!=UNINIT) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            targetPermanent.setDamage(oldDamage);
        }
        if (oldPrevent!=UNINIT) {
            target.setPreventDamage(oldPrevent);
        }
    }
}
