[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NegOther("target creature with shadow", permanent),
                MagicExileTargetPicker.create(),
                this,
                "Exile another target creature with shadow\$."
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
