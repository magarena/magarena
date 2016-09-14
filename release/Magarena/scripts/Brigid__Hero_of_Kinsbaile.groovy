[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "SN deals 2 damage to each attacking or blocking creature target player\$ controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL.filter(it) each {
                    final MagicPermanent target ->
                    game.doAction(new DealDamageAction(event.getSource(),target,2));
                }
            });
        }
    }
]
