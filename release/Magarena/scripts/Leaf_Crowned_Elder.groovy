def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCard it = event.getRefCard();
    final MagicPlayer player = event.getPlayer();
    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
        if (cevent.isSatisfied() == false) {
            game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay additional casting costs.");
            return;
        }
    }
    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
        game.addEvent(cevent);
    }
    game.doAction(new RemoveCardAction(it, MagicLocationType.OwnersLibrary));
    game.addEvent(new MagicPutCardOnStackEvent(
        it, 
        player, 
        MagicLocationType.OwnersLibrary, 
        MagicLocationType.Graveyard
    ));
}

[
    MagicAtYourUpkeepTrigger.kinship(
        "play that card without paying its mana cost.", 
        action
    )
]
