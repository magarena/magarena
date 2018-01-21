def CARD_WITH_CMC_LEQ_5_FROM_HAND = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getConvertedCost() <= 5;
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
            new MagicFromCardFilterChoice(CARD_WITH_CMC_LEQ_5_FROM_HAND, 1, false, "a card with converted mana cost 2 or less from your hand"),
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
                "PN draws cards equal to the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {

            final int amount = event.getPlayer().getPermanents().max({ it.getPower() }).getPower();
            game.doAction(new DrawAction(event.getPlayer(), amount));

            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice("Cast a card?"),
                mayAction,
                "PN may\$ cast a card with converted mana cost 5 or less from PN's hand without paying its mana cost."
            ));
        }
    }
]

