[
    new MagicWhenBecomesBlockedTrigger() {

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    source,
                    this,
                    "Prevent all combat damage that would be dealt this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAddTurnTriggerAction(
                MagicIfDamageWouldBeDealtTrigger.PreventCombatDamage
            ));
        }
    }
]
