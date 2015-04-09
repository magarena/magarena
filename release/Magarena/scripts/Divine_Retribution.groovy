[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ATTACKING_CREATURE,
                this,
                "SN deals damage to target attacking creature\$ equal to the" +
                "number of attacking creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(ATTACKING_CREATURE);
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
