[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(MagicSimpleMayChoice.OPPONENT_LOSE_LIFE),
                    this,
                    "PN may\$ reveal the top card of his or her library. If PN does, "
                    +"each opponent loses life equal to that card's converted mana cost."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player = event.getPlayer();
                for (final MagicCard card : player.getLibrary().getCardsFromTop(1)) {
                    final int amount = card.getConvertedCost();
                    game.doAction(new RevealAction(card));
                    game.logAppendMessage(player,"("+amount+")");
                    game.doAction(new ChangeLifeAction(player.getOpponent(), -amount));
                }
            }
        }
    }
]
