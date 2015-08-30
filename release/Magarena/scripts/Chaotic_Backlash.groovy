[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "SN deals damage to target player\$ equal to twice the number of white and/or blue permanents he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = it.getNrOfPermanents(MagicTargetFilterFactory.WHITE_OR_BLUE_PERMANENT_YOU_CONTROL) * 2;
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
