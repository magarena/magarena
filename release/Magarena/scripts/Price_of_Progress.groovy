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
                final int amount1 = event.getPlayer().getNrOfPermanents(MagicTargetFilterFactory.NONBASIC_LAND) * 2;
                game.doAction(new MagicDealDamageAction(event.getSource(), event.getPlayer(), amount1));
                final int amount2 = event.getPlayer().getOpponent().getNrOfPermanents(MagicTargetFilterFactory.NONBASIC_LAND) * 2;
                game.doAction(new MagicDealDamageAction(event.getSource(), event.getPlayer().getOpponent(), amount2));
        }
    }
]
