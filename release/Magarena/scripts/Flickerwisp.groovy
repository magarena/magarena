[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
         public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.PERMANENT,
                    permanent
                ),
                MagicTargetHint.Negative,
                "another target permanent"
            );            
         return new MagicEvent(
                permanent,
                targetChoice,
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
