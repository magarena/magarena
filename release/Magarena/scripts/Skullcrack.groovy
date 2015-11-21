def cantGainLife = new IfLifeWouldChangeTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
        if (act.getLifeChange() > 0) {
            act.setLifeChange(0);
        }
        return MagicEvent.NONE;
    }
}

def cantBePrevented = new IfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        damage.setUnpreventable();
        return MagicEvent.NONE;
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Players can't gain life this turn. Damage can't be prevented this turn. " +
                "SN deals 3 damage to target player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new AddTurnTriggerAction(cantGainLife));
                game.doAction(new AddTurnTriggerAction(cantBePrevented));
                game.doAction(new DealDamageAction(event.getSource(),it,3));
            });
        }
    }
]
