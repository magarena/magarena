[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Return"
    ){
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{1}{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicFromCardFilterChoice(CARD_FROM_GRAVEYARD,4,true,"to put on the bottom of your library"),
                this,
                "PN puts up to 4 target cards from his or her graveyard on the bottom of his or her library. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.BottomOfOwnersLibrary));
            });
        }
    }
]
