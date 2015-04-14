[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    NegOther(
                        "target creature",
                        permanent
                    )
                ),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ExileLinkAction(
                        event.getPermanent(),
                        it
                    ));
                });
            }
        }
    }
]
