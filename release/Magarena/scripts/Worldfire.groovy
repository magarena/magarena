[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile all permanents. Exile all cards from all hands and graveyards. Each player's life total becomes 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveAllFromPlayAction(
                PERMANENT.filter(event),
                MagicLocationType.Exile
            ));
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard it : hand) {
                    game.doAction(new ShiftCardAction(it,MagicLocationType.OwnersHand,MagicLocationType.Exile));
                }
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard it : graveyard) {
                    game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.Exile));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final int changeLife = 1 - player.getLife();
                game.doAction(new ChangeLifeAction(player,changeLife));
            }
        }
    }
]
