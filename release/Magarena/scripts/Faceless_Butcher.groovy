[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE,
                    permanent
                ),
                MagicTargetHint.None,
                "another target creature to exile"
            );
            return new MagicEvent(
                permanent,
                targetChoice,
                MagicExileTargetPicker.create(),
                this,
                "Exile another target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicExileUntilThisLeavesPlayAction(event.getPermanent(),creature));
                }
            });
        }
    }
]
