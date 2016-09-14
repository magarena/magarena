[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Tapping,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source, "{4}{U}{U}")
            ];
        }
    },
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Simultaneously untap all tapped creatures and tap all untapped creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicPermanent> tapped = TAPPED_CREATURE.filter(event);
            final List<MagicPermanent> untapped = UNTAPPED_CREATURE.filter(event);
            for (final MagicPermanent it : tapped) {
                game.doAction(new UntapAction(it));
            }
            for (final MagicPermanent it : untapped) {
                game.doAction(new TapAction(it));
            }
        }
    }
]
