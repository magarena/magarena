[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage to target creature or player\$ equal to the number of attacking creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(ATTACKING_CREATURE_YOU_CONTROL);
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
