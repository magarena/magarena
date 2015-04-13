[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player's life total becomes the number of creatures he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int creatures = player.getNrOfPermanents(MagicType.Creature);
                game.doAction(new ChangeLifeAction(player,creatures-player.getLife()));
            }
        }
    }
]
