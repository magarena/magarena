[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                damage.getTarget().isPlayer() &&
                damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS
                    ),
                    MagicBounceTargetPicker.create(),
                    this,
                    "PN may\$ return target creature defending player controls to its owner's hand."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
