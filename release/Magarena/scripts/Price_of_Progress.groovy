[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each player equal to twice the number of nonbasic lands that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(
                    event.getSource(), 
                    player, 
                    player.getNrOfPermanents(NONBASIC_LAND) * 2
                ));
            }
        }
    }
]
