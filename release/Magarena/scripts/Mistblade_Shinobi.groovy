[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS
                ),
                MagicBounceTargetPicker.create(),
                this,
                "PN may\$ return target creature defending player controls to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
