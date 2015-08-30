def cantBePrevented = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
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
                this,
                "Damage can't be prevented this turn. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(cantBePrevented));
        }
    }
]
