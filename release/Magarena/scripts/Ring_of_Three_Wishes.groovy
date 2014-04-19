def CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return true;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_CARD_FROM_LIBRARY = new MagicTargetChoice(
    CARD_FROM_LIBRARY,
    "a card"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Search"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{5}"),
                new MagicTapEvent(source),
                new MagicRemoveCounterEvent(source,MagicCounterType.Wish,1)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN searches PN's library for a card and puts it into PN's hand. Then shuffles PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                A_CARD_FROM_LIBRARY,
                MagicLocationType.OwnersHand
            ));
        }
    }
]
