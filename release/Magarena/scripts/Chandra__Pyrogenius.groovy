[
    new MagicPlaneswalkerActivation(-10) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(6),
                this,
                "SN deals 6 damage to target player\$ and each creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 6));
                CREATURE_YOU_CONTROL.filter(it) each {
                    final MagicPermanent target ->
                    game.doAction(new DealDamageAction(event.getSource(), target, 6));
                }
            });
        }
    }
]
