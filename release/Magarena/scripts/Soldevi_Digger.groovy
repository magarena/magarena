[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicConditionFactory.GraveyardAtLeast(1))],
        new MagicActivationHints(MagicTiming.Draw),
        "Card"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put the top card of PN's graveyard on the bottom of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top1 = event.getPlayer().getGraveyard().getCardsFromTop(1) ;
            for (final MagicCard top : top1) {
                game.doAction(new ShiftCardAction(
                    top,
                    MagicLocationType.Graveyard,
                    MagicLocationType.BottomOfOwnersLibrary
                ));
                game.logAppendMessage(event.getPlayer()," ("+top.getName()+")");
            }
        }
    }
]
