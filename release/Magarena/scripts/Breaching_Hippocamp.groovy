[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice TARGET_OTHER_CREATURE_YOU_CONTROL=new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    permanent
                ),
                MagicTargetHint.None,
                "a creature other than " + permanent + " to untap"
            );
            return new MagicEvent(
                permanent,
                TARGET_OTHER_CREATURE_YOU_CONTROL,
                MagicTapTargetPicker.Untap,
                this,
                "Untap another target creature you control\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicUntapAction(creature));
            });
        }
    }
]
