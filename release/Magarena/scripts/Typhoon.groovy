[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each opponent equal to the number of Islands that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent=event.getPlayer().getOpponent();
            final int amount=opponent.getNrOfPermanents(MagicSubType.Island);
            game.doAction(new MagicDealDamageAction(event.getSource(),opponent,amount));
        }
    }
]
