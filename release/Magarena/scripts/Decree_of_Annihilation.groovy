[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile all artifacts, creatures, and lands from the battlefield, all cards from all graveyards, and all cards from all hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList all = new MagicPermanentList();
            all.addAll(ARTIFACT.filter(event));
            all.addAll(CREATURE.filter(event));
            all.addAll(LAND.filter(event));
            game.doAction(new RemoveAllFromPlayAction(all, MagicLocationType.Exile));

            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard it : graveyard) {
                    game.doAction(new ShiftCardAction(
                        it,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                }
            }

            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard it : hand) {
                    game.doAction(new ShiftCardAction(
                        it,
                        MagicLocationType.OwnersHand,
                        MagicLocationType.Exile
                    ));
                }
            }
        }
    }
]
