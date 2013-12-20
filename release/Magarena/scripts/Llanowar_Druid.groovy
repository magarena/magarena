[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Untap"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Untap all forest."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_FOREST);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    }
]
