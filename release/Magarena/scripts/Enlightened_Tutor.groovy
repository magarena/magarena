def ARTIFACT_OR_ENCHANTMENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Artifact) || target.hasType(MagicType.Enchantment);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_ARTIFACT_OR_ENCHANTMENT_CARD_FROM_LIBRARY = new MagicTargetChoice(
    ARTIFACT_OR_ENCHANTMENT_CARD_FROM_LIBRARY,
    "an artifact or enchantment card"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for an artifact or enchantment card, reveals it, shuffle his or her library, and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoLibraryEvent(
                event,
                AN_ARTIFACT_OR_ENCHANTMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
