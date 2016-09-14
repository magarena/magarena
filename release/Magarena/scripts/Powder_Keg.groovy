def makeFilter = {
    final int fuse ->
    return new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.getConvertedCost() == fuse && (target.isArtifact() || target.isCreature())
        }
    };
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
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
            final int fuse = event.getPermanent().getCounters(MagicCounterType.Fuse);
            game.doAction(new DestroyAction(makeFilter(fuse).filter(event)));
        }
    }
]
