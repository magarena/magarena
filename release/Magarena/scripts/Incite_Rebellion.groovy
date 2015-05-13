[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each player, SN deals damage to that player and each creature that player controls "+
                "equal to the number of creatures he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = player.getNrOfPermanents(MagicType.Creature);
                game.doAction(new DealDamageAction(event.getSource(), player, amount));
                CREATURE_YOU_CONTROL.filter(player) each {
                    game.doAction(new DealDamageAction(event.getSource(), it, amount));
                }
            }
        }
    }
]
