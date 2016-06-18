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
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicDiscardChosenEvent(source,A_CARD_NAMED_BARU)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts an X/X green Wurm creature token onto the battlefield, where X is the number of lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getPlayer().getNrOfPermanents(MagicType.Land);
            game.doAction(new PlayTokenAction(
                event.getPlayer(), 
                MagicCardDefinition.create(
                    CardDefinitions.getToken("green Wurm creature token"),
                    {
                        it.setPowerToughness(x, x);
                        it.setValue(x);
                    }
                )
            ));
        }
    }
]
