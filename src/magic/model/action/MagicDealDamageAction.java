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
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTriggerType;
import magic.model.event.MagicRedirectDamageEvent;

public class MagicDealDamageAction extends MagicAction {
    
    private static final int UNINIT = Integer.MIN_VALUE;

    private final MagicDamage damage;
    private MagicTarget target;
    private int oldDamage = UNINIT;
    private int oldPrevent = UNINIT;
    
    public MagicDealDamageAction(final MagicDamage damage) {
        this.damage=damage;
    }
    
    public MagicDealDamageAction(final MagicDamage damage, final MagicTarget target) {
        this.damage=damage;
        this.target=target;
    }

    private int preventDamage(final MagicGame game,int amount) {
        // Damage is not preventable.
        if (damage.isUnpreventable()) {
            return amount;
        }
        // Prevent all damage.
        if (target.getController().hasState(MagicPlayerState.PreventAllDamage)) {
            return 0;
        }
        // Prevent all combat damage.
        if (target.getController().hasState(MagicPlayerState.PreventAllCombatDamage) &&
            damage.isCombat()) {
            return 0;
        }
        if (target.isPermanent()) {
            final MagicPermanent targetPermanent = (MagicPermanent)target;
            if (targetPermanent.hasProtectionFrom(damage.getSource()) ||
                targetPermanent.hasState(MagicPermanentState.PreventAllDamage)) {
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
        /*
        306.7. If damage would be dealt to a player by a source
        controlled by an opponent, that opponent may have that source deal that
        damage to a planeswalker the first player controls instead.
        */
        if (target == null &&
            damage.getTarget().isPlayer() && 
            damage.getSource().getController() != damage.getTarget() &&
            damage.getTarget().getController().getNrOfPermanentsWithType(MagicType.Planeswalker) > 0) {
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

        game.executeTrigger(MagicTriggerType.IfDamageWouldBeDealt,damage);
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
            game.doAction(new MagicChangeCountersAction(targetPermanent,MagicCounterType.Charge,-dealtAmount,true));
        } 
        
        if (target.isCreature()) {
            final MagicPermanent targetPermanent=(MagicPermanent)target;
            if (damage.hasNoRegeneration()) {
                game.doAction(new MagicChangeStateAction(targetPermanent,MagicPermanentState.CannotBeRegenerated,true));
            }
            if (source.hasAbility(MagicAbility.Wither)||source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new MagicChangeCountersAction(targetPermanent,MagicCounterType.MinusOne,dealtAmount,true));
            } else {
                oldDamage=targetPermanent.getDamage();
                targetPermanent.setDamage(oldDamage+dealtAmount);
            }
            if (source.hasAbility(MagicAbility.Deathtouch)) {
                game.doAction(new MagicChangeStateAction(targetPermanent,MagicPermanentState.Destroyed,true));
            }
        } 
        
        if (target.isPlayer()) {
            final MagicPlayer targetPlayer = (MagicPlayer)target;
            if (source.hasAbility(MagicAbility.Infect)) {
                game.doAction(new MagicChangePoisonAction(targetPlayer,dealtAmount));
            } else {
                game.doAction(new MagicChangeLifeAction(targetPlayer,-dealtAmount));
            }
            game.doAction(new MagicChangePlayerStateAction(targetPlayer,MagicPlayerState.WasDealtDamage));
        }

        damage.setDealtAmount(dealtAmount);
        if (source.hasAbility(MagicAbility.LifeLink)) {
            game.doAction(new MagicChangeLifeAction(source.getController(),dealtAmount));
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
