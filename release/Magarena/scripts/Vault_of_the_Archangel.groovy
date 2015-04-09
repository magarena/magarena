[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{2}{W}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures PN controls gain deathtouch and lifelink until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicGainAbilityAction(
                    creature,
                    [
                        MagicAbility.Deathtouch,
                        MagicAbility.Lifelink
                    ]
                ));
            }
        }
    }
]
