[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
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
                "Destroy each artifact and creature with converted mana cost equal to the number of fuse counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicPermanent> artifacts = game.filterPermanents(MagicTargetFilterFactory.ARTIFACT);
            for (final MagicPermanent artifact : artifacts) {
                if (artifact.getConvertedCost() == event.getPermanent().getCounters(MagicCounterType.Fuse)) {   
                    game.doAction(new MagicDestroyAction(artifact)); 
                }
            }
            final List<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.CREATURE);
            for (final MagicPermanent creature : creatures) {
                if (creature.getConvertedCost() == event.getPermanent().getCounters(MagicCounterType.Fuse)) {   
                    game.doAction(new MagicDestroyAction(creature)); 
                }
            }
        }
    }
]
