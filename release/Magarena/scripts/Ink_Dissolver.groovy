def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicMillLibraryAction(event.getPlayer().getOpponent(),3));
}

[
    MagicAtYourUpkeepTrigger.kinship(
        "each opponent puts the top three cards of his or her library into his or her graveyard.",
        action
    )
]
