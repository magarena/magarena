def cantGainLife = new MagicIfLifeWouldChangeTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeLifeAction act) {
        if (act.getLifeChange() > 0) {
            act.setLifeChange(0);
        }
        return MagicEvent.NONE;
    }
}

def cantBePrevented = new MagicIfDamageWouldBeDealtTrigger() {
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
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Players can't gain life this turn. Damage can't be prevented this turn. " +
                "SN deals 3 damage to target player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicAddTurnTriggerAction(cantGainLife));
                game.doAction(new MagicAddTurnTriggerAction(cantBePrevented));
                game.doAction(new MagicDealDamageAction(event.getSource(),it,3));
            });
        }
    }
]
