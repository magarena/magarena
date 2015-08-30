def choice = new MagicTargetChoice("target attacking creature with shadow");

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
                choice,
                MagicTapTargetPicker.Untap,
                this,
                "Untap target attacking creature with shadow\$. " +
                "Prevent all combat damage that would be dealt to and dealt by it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new UntapAction(it));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    MagicPreventDamageTrigger.PreventCombatDamageDealtToDealtBy
                ));
            });
        }
    }
]
