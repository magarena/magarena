[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Strike"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{R}"),
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_SAMURAI
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Samurai creatures PN controls gain double strike until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_SAMURAI_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.DoubleStrike));
            }
        }
    }
]
