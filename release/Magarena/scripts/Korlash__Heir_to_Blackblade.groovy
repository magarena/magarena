def CARD_NAMED_KORLASH = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Korlash, Heir to Blackblade");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};
def A_CARD_NAMED_KORLASH = new MagicTargetChoice(
    CARD_NAMED_KORLASH,
    MagicTargetHint.None,
    "a card named Korlash, Heir to Blackblade from your hand"
);

def SWAMP_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Swamp);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Grandeur"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicDiscardChosenEvent(source,A_CARD_NAMED_KORLASH)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for up to two Swamp cards, "+
                "puts them onto the battlefield tapped, then shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    SWAMP_CARD_FROM_LIBRARY,
                    2,
                    true,
                    "to put onto the battlefield tapped"
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]
