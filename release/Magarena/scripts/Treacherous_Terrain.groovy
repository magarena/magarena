[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each opponent equal to the number of lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            final int amount = opponent.getNrOfPermanents(MagicType.Land);
            game.logAppendValue(event.getPlayer(), amount);
            game.doAction(new DealDamageAction(event.getSource(),opponent,amount));
        }
    }
]
