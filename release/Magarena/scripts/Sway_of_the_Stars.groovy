[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player shuffles his or her hand, graveyard, and permanents he or she owns into his or her library, then draws seven cards. Each player's life total becomes 7."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard it : hand) {
                    game.doAction(new ShiftCardAction(it,MagicLocationType.OwnersHand,MagicLocationType.OwnersLibrary));
                }
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard it : graveyard) {
                    game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
                }
                game.doAction(new RemoveAllFromPlayAction(
                    PERMANENT_YOU_OWN.filter(player),
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new DrawAction(player,7));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final int changeLife = 7 - player.getLife();
                game.doAction(new ChangeLifeAction(player,changeLife));
            }
        }
    }
]
