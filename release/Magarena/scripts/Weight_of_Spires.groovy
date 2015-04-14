[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals damage to target creature\$ equal to the number of nonbasic lands that creature's controller controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getController().getNrOfPermanents(NONBASIC_LAND)
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
