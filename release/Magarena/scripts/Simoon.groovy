[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "SN deals 1 damage to each creature target player\$ " +
                "controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                CREATURE_YOU_CONTROL.filter(it) each {
                    game.doAction(new DealDamageAction(event.getSource(), it, 1));
                }
            });
        }
    }
]
