[
    new LandfallTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    NEG_TARGET_CREATURE
                ),
                this,
                "PN may\$ gain control of target creature\$ for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new AddStaticAction(
                        event.getPermanent(),
                        MagicStatic.ControlAsLongAsYouControlSource(
                            event.getPlayer(),
                            it
                        )
                    ));
                });
            }
        }
    }
]
