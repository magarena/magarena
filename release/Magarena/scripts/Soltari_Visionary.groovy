[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    permanent.isController(damage.getTarget()) ?
                        MagicTargetChoice.TARGET_ENCHANTMENT_YOU_CONTROL :
                        MagicTargetChoice.TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target enchantment\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicDestroyAction(perm));
                }
            });
        }
    }
]
