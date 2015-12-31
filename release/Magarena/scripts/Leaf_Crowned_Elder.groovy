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
    AtYourUpkeepTrigger.kinship("play that card without paying its mana cost.", action)
]
