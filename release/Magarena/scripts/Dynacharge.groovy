[
    new MagicOverloadActivation(MagicTiming.Pump) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature you control gets +2/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature, 2, 0));
            }
        }
    }
]
