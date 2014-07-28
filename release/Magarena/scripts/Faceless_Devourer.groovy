[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_WITH_SHADOW,
                    permanent
                ),
                MagicTargetHint.None,
                "another target creature with shadow to exile"
            );
            return new MagicEvent(
                permanent,
                targetChoice,
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
