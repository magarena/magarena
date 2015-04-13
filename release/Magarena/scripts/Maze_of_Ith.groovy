def TARGET_ATTACKING_CREATURE = new MagicTargetChoice(
    "target attacking creature"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Untap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_ATTACKING_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "Untap target attacking creature\$. " +
                "Prevent all combat damage that would be dealt to and dealt by it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicUntapAction(it));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    MagicIfDamageWouldBeDealtTrigger.PreventCombatDamageDealtToDealtBy
                ));
            });
        }
    }
]
