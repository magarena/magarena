[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player loses 1 life for each creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = player.getNrOfPermanents(MagicType.Creature);
                game.logAppendMessage(event.getPlayer(),player.getName()+" loses "+amount+" life.")
                game.doAction(new ChangeLifeAction(player, -amount));
            }
        }
    }
]
