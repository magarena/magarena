[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each player equal to the number of lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source=event.getSource();
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = player.getNrOfPermanents(MagicType.Land);
                game.doAction(new MagicDealDamageAction(source,player,amount));
            }
        }
    }
]
