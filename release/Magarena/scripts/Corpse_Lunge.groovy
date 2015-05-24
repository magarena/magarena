[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to (RN)'s power to target creature.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getRefCard().getPower();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
