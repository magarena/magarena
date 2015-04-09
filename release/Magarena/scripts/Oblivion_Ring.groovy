[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NegOther(
                    "target nonland permanent", 
                    permanent
                ),
                MagicExileTargetPicker.create(),
                this,
                "Exile another target nonland permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileLinkAction(event.getPermanent(),it));
            });
        }
    }
]
