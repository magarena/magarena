[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.PumpFlash),
        "+Abilities"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicDiscardEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gains shroud until end of turn and can't be blocked this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Shroud));
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Unblockable));
        }
    }
]
