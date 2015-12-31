[
    new IfPlayerWouldLoseTrigger() {
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

            final MagicCardList hand = new MagicCardList(player.getHand());
            for (final MagicCard card : hand) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersHand,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            };

            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            };

            game.doAction(new RemoveAllFromPlayAction(
                PERMANENT_YOU_OWN.filter(event),
                MagicLocationType.TopOfOwnersLibrary
            ));

            game.doAction(new ShuffleLibraryAction(player));
            game.doAction(new DrawAction(player,7));

            final int lifeChange = 20 - player.getLife();
            game.doAction(new ChangeLifeAction(player, lifeChange));
        }
    }
]
