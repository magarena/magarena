[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
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
                    game.doAction(new MagicSacrificeAction(it));
                });
            } else {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    }
]
