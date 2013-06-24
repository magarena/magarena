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
                        MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS
                    ),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "PN may\$ destroy target artifact or enchantment\$ an opponent controls."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent permanent) {
                        game.doAction(new MagicDestroyAction(permanent));
                    }
                });
            }
        }
    }
]
