[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction data) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainControlAction(event.getPlayer(),creature));
                }
            });
        }
    }
]
