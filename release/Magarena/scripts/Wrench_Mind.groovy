def ARTIFACT_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Artifact);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};

def AN_ARTIFACT_CARD_FROM_HAND = new MagicTargetChoice(
    ARTIFACT_CARD_FROM_HAND,
    MagicTargetHint.None,
    "an artifact card from your hand"
);

def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent discardEvent = new MagicDiscardChosenEvent(event.getSource(), event.getPlayer(), AN_ARTIFACT_CARD_FROM_HAND)
    if (event.isYes() && discardEvent.isSatisfied()) {
        game.addEvent(discardEvent);
    } else {
        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 2));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards two cards unless he or she discards an artifact card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Discard an artifact card?"),
                    action,
                    ""
                ));
            })
        }
    }
]
