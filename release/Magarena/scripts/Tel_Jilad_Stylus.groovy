[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Library"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_PERMANENT_YOU_OWN,
                this,
                "PN puts target permanent he or she owns\$ on the bottom of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.BottomOfOwnersLibrary));
            });
        }
    }
]
