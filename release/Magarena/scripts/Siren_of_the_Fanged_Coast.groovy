[
    new MagicTributeTrigger(3) {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                {
                    final MagicGame game, final MagicEvent event ->
                    event.processTargetPermanent(game, {
                        final MagicPermanent perm ->
                        game.doAction(new MagicGainControlAction(
                            event.getPlayer(),
                            perm
                        ));
                    });
                },
                "PN gains control of target creature\$."
            );
        }
    }
]
