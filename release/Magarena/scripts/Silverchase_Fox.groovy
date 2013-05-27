[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{1}{W}")],
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostSacrificeEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{1}{W}")
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                new MagicDestroyTargetPicker(false),
                this,
                "Exile target enchantment\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
                }
            });
        }
    }
]
