[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Unblockable"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN can't be blocked this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Fear));
        }
    }
]
