def CARD_NAMED_NISSA_NATURE_S_ARTISAN_FROM_YOUR_LIBRARY_OR_GRAVEYARD = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("Nissa, Nature's Artisan");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library || targetType == MagicTargetType.Graveyard;
    }
}

def searchAction = {
    final MagicGame game, final MagicEvent event ->
    game.addEvent(new MagicSearchToLocationEvent(
        event,
        new MagicFromCardFilterChoice(
            CARD_NAMED_NISSA_NATURE_S_ARTISAN_FROM_YOUR_LIBRARY_OR_GRAVEYARD,
            1,
            false,
            "a card named Nissa, Nature's Artisan from your library or graveyard"
        ),
        MagicLocationType.OwnersHand
    ));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                ""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicFromCardFilterChoice(BASIC_LAND_CARD_FROM_LIBRARY, 1, false, "a basic land card from your library"),
                { game.doAction(new TapAction.Enters(it)) }
            ));
            game.doAction(searchAction);
        }
    }
]

