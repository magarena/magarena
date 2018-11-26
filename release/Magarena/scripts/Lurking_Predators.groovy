def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
    final MagicCard card = event.getRefCard();
    final MagicPlayer player = event.getPlayer();
        game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));
        game.logAppendMessage(player, "${player} moved (${card})  to the bottom.")
    }
}

[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN reveal the top card of his or her library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Creature) == true) {
                game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary, card, event.getPlayer()));
               } else {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicMayChoice("Put ${card} on the bottom of your library ?"),
                        card,
                        action,
                        "PN may\$ put that card on the bottom of his or her library."
                    ));
                }
            }
        }
    }
]
