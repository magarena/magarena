[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS
                ),
                MagicDestroyTargetPicker.Destroy,
                this,
                "PN may\$ destroy target artifact or enchantment\$ an opponent controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DestroyAction(it));
                });
            }
        }
    }
]
