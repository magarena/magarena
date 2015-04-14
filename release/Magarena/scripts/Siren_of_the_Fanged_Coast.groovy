[
    new MagicTributeTrigger(3) {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
