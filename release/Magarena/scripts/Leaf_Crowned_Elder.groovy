def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(CastCardAction.WithoutManaCost(
        event.getPlayer(), 
        event.getRefCard(), 
        MagicLocationType.OwnersLibrary, 
        MagicLocationType.Graveyard
    ));
}

[
    MagicAtYourUpkeepTrigger.kinship("play that card without paying its mana cost.", action)
]
