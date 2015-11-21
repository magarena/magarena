def action = {
    final MagicGame game, final MagicEvent event ->
    for (final MagicPlayer player : game.getAPNAP()) {
        final MagicCardList hand = new MagicCardList(player.getHand());
        for (final MagicCard card : hand) {
            game.doAction(new DiscardCardAction(player,card));
        }
        game.doAction(new DrawAction(player, 4));
    }
}
                            
[
    AtYourUpkeepTrigger.kinship(
        "each player discards his or her hand, then draws four cards.", 
        action
    )
]
