def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicRemoveCardAction(event.getRefCard(),MagicLocationType.OwnersLibrary));
    final MagicCardOnStack cardOnStack=new MagicCardOnStack(event.getRefCard(),event.getPlayer(),MagicPayedCost.NO_COST);
    cardOnStack.setFromLocation(MagicLocationType.OwnersLibrary);
    game.doAction(new PutItemOnStackAction(cardOnStack));
}

[
    MagicAtYourUpkeepTrigger.kinship(
        "play that card without paying its mana cost.", 
        action
    )
]
