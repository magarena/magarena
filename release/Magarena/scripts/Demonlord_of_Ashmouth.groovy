[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.TARGET_CREATURE_YOU_CONTROL,
                    permanent
                ),
                MagicTargetHint.None,
                "another creature to sacrifice"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicSacrificeTargetPicker.create(),
                this,
                "PN may\$ sacrifice another creature\$. If you don't, exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    game.doAction(new MagicSacrificeAction(creature));
                });
            } else {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    }
]
