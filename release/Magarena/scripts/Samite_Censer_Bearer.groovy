[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostSacrificeEvent(source, "{W}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Prevent the next 1 damage that would be dealt to each creature you control this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicPreventDamageAction(creature,1));
            }
        }
    }
]
