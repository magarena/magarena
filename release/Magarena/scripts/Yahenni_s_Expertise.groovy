def CARD_WITH_CMC_LEQ_3_FROM_HAND = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getConvertedCost() <= 3;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}

def castAction = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetCard(game, {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersHand, MagicLocationType.Graveyard));
    });
}

def mayAction = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicEvent(
            event.getSource(),
            new MagicFromCardFilterChoice(CARD_WITH_CMC_LEQ_3_FROM_HAND, 1, false, "a card with converted mana cost 3 or less from your hand"),
            castAction,
            "\$"
        ));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -3/-3 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
            for (final MagicPermanent perm : player.getPermanents()) {
                if (perm.hasType(MagicType.Creature)) {
                    game.doAction(new ChangeTurnPTAction(perm, -3, -3));
                }
            }
            }
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice("Cast a card?"),
                mayAction,
                "PN may\$ cast a card with converted mana cost 3 or less from PN's hand without paying its mana cost."
            ));
        }
    }
]

