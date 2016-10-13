def CARD_NAMED_BARU = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Baru, Fist of Krosa");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};
def A_CARD_NAMED_BARU = new MagicTargetChoice(
    CARD_NAMED_BARU,
    MagicTargetHint.None,
    "a card named Baru, Fist of Krosa from your hand"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Grandeur"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicDiscardChosenEvent(source,A_CARD_NAMED_BARU)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN creates an X/X green Wurm creature token, where X is the number of lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getPlayer().getNrOfPermanents(MagicType.Land);
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "green Wurm creature token")
            ));
        }
    }
]
