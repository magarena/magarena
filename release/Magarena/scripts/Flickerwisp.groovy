[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
         public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                Other("target permanent", permanent),
                MagicExileTargetPicker.create(),
                this,
                "Exile another target permanent\$. Return that card to the " +
                "battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileUntilEndOfTurnAction(it));
            });
        }
    }
]
