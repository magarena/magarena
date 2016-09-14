[
    new MagicOverloadActivation(MagicTiming.Removal) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{6}{U}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return each nonland permanent PN doesn't control to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveAllFromPlayAction(
                NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS.filter(event),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
