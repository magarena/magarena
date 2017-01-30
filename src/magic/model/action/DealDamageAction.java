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

public class DealDamageAction extends MagicAction {

    private static final int UNINIT = Integer.MIN_VALUE;

    private final MagicDamage damage;
    private MagicTarget target;
    private int oldDamage = UNINIT;

    public DealDamageAction(final MagicSource source, MagicTarget target, final int amt) {
        this(new MagicDamage(source, target, amt), null);
    }

    public DealDamageAction(final MagicDamage damage) {
        this(damage, null);
    }

    public static final DealDamageAction NoRedirect(final MagicDamage damage) {
        return new DealDamageAction(damage, damage.getTarget());
    }

    private DealDamageAction(final MagicDamage aDamage, final MagicTarget aTarget) {
        damage = aDamage;
        target = aTarget;
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
        if (!target.isCreaturePermanent() &&
            !target.isPlaneswalkerPermanent() &&
            !target.isPlayer()) {
            return;
        }

        damage.setDealtAmount(0);
        final int dealtAmount=damage.getAmount();
        if (dealtAmount <= 0) {
            return;
        }

        final MagicSource source=damage.getSource();

        if (target.isPlaneswalkerPermanent()) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            game.doAction(new ChangeCountersAction(targetPermanent,MagicCounterType.Loyalty,-dealtAmount));
        }

        if (target.isCreaturePermanent()) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            if (damage.hasNoRegeneration()) {
                game.doAction(ChangeStateAction.Set(targetPermanent,MagicPermanentState.CannotBeRegenerated));
            }
            if (source.hasAbility(MagicAbility.Wither)||source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new ChangeCountersAction(targetPermanent,MagicCounterType.MinusOne,dealtAmount));
            } else {
                oldDamage=targetPermanent.getDamage();
                targetPermanent.setDamage(oldDamage+dealtAmount);
            }
            if (source.hasAbility(MagicAbility.Deathtouch)) {
                game.doAction(ChangeStateAction.Set(targetPermanent,MagicPermanentState.Destroyed));
            }
            game.doAction(ChangeStateAction.Set(targetPermanent,MagicPermanentState.WasDealtDamage));
        }

        if (target.isPlayer()) {
            final MagicPlayer targetPlayer = (MagicPlayer)target;
            if (source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new ChangeCountersAction(targetPlayer, MagicCounterType.Poison, dealtAmount));
            } else {
                game.doAction(new ChangeLifeAction(targetPlayer,-dealtAmount,true));
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
    }
}
