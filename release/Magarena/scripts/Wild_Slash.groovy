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
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "If PN controls a creature with power 4 or greater, damage can't be prevented this turn. " +
                "SN deals 2 damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                if (event.getPlayer().controlsPermanent(CREATURE_POWER_4_OR_MORE)) {
                    game.doAction(new AddTurnTriggerAction(cantBePrevented));
                }
                game.doAction(new DealDamageAction(event.getSource(),it,2));
            });
        }
    }
]
