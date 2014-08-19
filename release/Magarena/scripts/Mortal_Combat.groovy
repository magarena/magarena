[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.filterCards(MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size() >= 20 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If twenty or more creature cards are in PN's graveyard, PN wins the game."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().filterCards(MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size() >= 20) {
                game.doAction(new MagicLoseGameAction(event.getPlayer().getOpponent()));
            }
        };
    }
]
