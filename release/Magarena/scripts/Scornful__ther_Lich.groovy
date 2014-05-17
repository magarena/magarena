[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Gains") {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{W}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gains fear and vigilance until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(
                event.getPermanent(),
                [MagicAbility.Fear, MagicAbility.Vigilance]
            ));
        }
    }
]
