def A_CARD_NAMED_VRASKA_SCHEMING_GORGON_FROM_YOUR_LIBRARY_OR_GRAVEYARD = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("Vraska, Scheming Gorgon");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library || targetType == MagicTargetType.Graveyard;
    }
}

def searchAction = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicSearchToLocationEvent(
            event,
            new MagicFromCardFilterChoice(
                A_CARD_NAMED_VRASKA_SCHEMING_GORGON_FROM_YOUR_LIBRARY_OR_GRAVEYARD,
                1,
                false,
                "a card named Vraska, Scheming Gorgon from your library or graveyard"
            ),
            MagicLocationType.OwnersHand
        ));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses 4 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it, 4));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Search your library?"),
                    searchAction,
                    "PN may\$ search PN's library and/or graveyard for a card named Vraska, Scheming Gorgon, reveal it, and put it into PN's hand. If PN search his or her library this way, shuffle it."
                ));
            });
        }
    }
]

