def ENCHANTMENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Enchantment);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_ENCHANTMENT_CARD_FROM_LIBRARY = new MagicTargetChoice(
    ENCHANTMENT_CARD_FROM_LIBRARY,
    "an enchantment card"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
           return new MagicEvent(
                source,
                this,
                "PN searches his or her library for an enchantment card, reveals it, then shuffle his or her library and put that card on top of it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoLibraryEvent(
                event,
                AN_ENCHANTMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
