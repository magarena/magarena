[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    NegOther(
                        "target nonland permanent",
                        permanent
                    )
                ),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another target nonland permanent\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ExileLinkAction(event.getPermanent(),it));
                });
            }
        }
    }
]
