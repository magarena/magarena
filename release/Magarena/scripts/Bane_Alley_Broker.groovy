def HAS_EXILED_BEFORE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicPermanent permanent = (MagicPermanent)source;
        return permanent.getExiledCards().size() > 0;
    }
};

def ExileCard = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(new ExileLinkAction(
            event.getPermanent(),
            it,
            MagicLocationType.OwnersHand
        ));
    });
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card, then exiles a card from his or her hand face down."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),1));
            game.addEvent(new MagicEvent(
                event.getSource(),
                A_CARD_FROM_HAND,
                MagicGraveyardTargetPicker.ExileOwn,
                ExileCard,
                "PN exiles a card from his or her hand face down."
            ));
        }
    },
    new MagicPermanentActivation(
        [HAS_EXILED_BEFORE_CONDITION],
        new MagicActivationHints(MagicTiming.Draw),
        "Reclaim"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{U}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicFromCardListChoice(permanent.getExiledCards(),1),
                this,
                "PN returns a card exiled with Bane Alley Broker to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                game.doAction(new ReclaimExiledCardAction(event.getPermanent(),it));
            });
        }
    }
]
