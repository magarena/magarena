[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            for (final MagicCard card : upkeepPlayer.getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicLookAction(card, upkeepPlayer, "top card of your library"));
                    for (final MagicSubType subtype : MagicSubType.values()) {
                    return card.hasSubType(subtype) == permanent.hasSubType(subtype) ?
                        new MagicEvent(
                            permanent,
                            new MagicMayChoice(),
                            card,
                            this,
                            "PN may\$ reveal the top card of PN's library. If you do, each player discards his or her hand, then draws four cards."
                        ):
                        MagicEvent.NONE;
                    }
                }
            }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicRevealAction(event.getRefCard()));
                    for (final MagicPlayer player : game.getAPNAP()) {
                    final MagicCardList hand = new MagicCardList(player.getHand());
                    for (final MagicCard card : hand) {
                        game.doAction(new MagicDiscardCardAction(player,card));
                    }
                    game.doAction(new MagicDrawAction(player, 4));
                }          
            }
        }
    }
]
