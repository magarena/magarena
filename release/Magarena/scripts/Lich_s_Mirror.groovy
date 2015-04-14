[
    new MagicIfPlayerWouldLoseTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final LoseGameAction loseAct) {
            final MagicPlayer losePlayer = loseAct.getPlayer();
            if (permanent.isController(losePlayer)) {
                loseAct.setPlayer(MagicPlayer.NONE);
            }
            return losePlayer == permanent.getController() ?
                new MagicEvent(
                    permanent,
                    losePlayer,
                    this,
                    "PN shuffles their hand, graveyard, and all permanents PN owns into PN's library. PN draws seven cards. PN's life becomes 20."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int lifeChange = 20 - player.getLife();
            game.doAction(new ChangeLifeAction(player, lifeChange));
            final MagicCardList hand = new MagicCardList(player.getHand());
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            final List<MagicPermanent> battlefield = player.filterPermanents(PERMANENT_YOU_OWN);
            for (final MagicCard card : hand) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersHand
                ));
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.OwnersHand,
                    MagicLocationType.OwnersLibrary
                ));
            };
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.Graveyard
                ));
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersLibrary
                ));
            };
            for (final MagicPermanent permanent : battlefield) {
                game.doAction(new MagicRemoveFromPlayAction(
                    permanent,
                    MagicLocationType.OwnersLibrary
                ));
            };
            game.doAction(new DrawAction(player,7));
        }
    }
]
