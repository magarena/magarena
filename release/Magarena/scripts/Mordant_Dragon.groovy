[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "PN may\$ have SN deal RN damage to target creature an opponent controls.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DealDamageAction(event.getSource(),it,event.getRefInt()));
                });
            }
        }
    }
]
