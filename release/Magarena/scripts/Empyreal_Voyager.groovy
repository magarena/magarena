def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeCountersAction(event.getPlayer(), MagicCounterType.Energy, event.getRefInt()));
}

[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getDealtAmount(),
                action,
                "When SN deals combat damage to a player, PN gets that many {E}."
            );
        }
    }
]

