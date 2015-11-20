[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "PN chooses a color.\$ SN deals deals damage to each player equal to "+
                "the number of creatures of that color that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = player.getNrOfPermanents(MagicType.Creature, event.getChosenColor());
                game.doAction(new DealDamageAction(event.getSource(), player, amount));
            }
        }
    }
]
