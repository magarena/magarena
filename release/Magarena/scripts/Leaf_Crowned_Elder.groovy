def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicRemoveCardAction(event.getRefCard(),MagicLocationType.OwnersLibrary));
    final MagicCardOnStack cardOnStack=new MagicCardOnStack(event.getRefCard(),event.getPlayer(),MagicPayedCost.NO_COST);
    game.doAction(new MagicPutItemOnStackAction(cardOnStack));
}

[
    MagicAtYourUpkeepTrigger.kinship(
        "play that card without paying its mana cost.", 
        action
    )
]
