[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                new MagicDamageTargetPicker(2),
                this,
                "PN may\$ have SN deal 2 damage to target creature\$ defending player controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DealDamageAction(event.getSource(),it,2));
                });
            }
        }
    }
]
