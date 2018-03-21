def A_CARD_NAMED_TEZZERET_MASTER_OF_METAL_FROM_YOUR_LIBRARY_OR_GRAVEYARD = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("Tezzeret, Master of Metal");
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
                A_CARD_NAMED_TEZZERET_MASTER_OF_METAL_FROM_YOUR_LIBRARY_OR_GRAVEYARD,
                1,
                false,
                "a card named Tezzeret, Master of Metal from your library or graveyard"
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
                NEG_TARGET_CREATURE,
                this,
                "Destroy target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Search for Tezzeret, Master of Metal?"),
                    searchAction,
                    "PN may\$ search PN's library and/or graveyard for a card named Tezzeret, Master of Metal, reveal it, and put it into PN's hand. If PN search his or her library this way, shuffle it."
                ));
            });
        }
    }
]

