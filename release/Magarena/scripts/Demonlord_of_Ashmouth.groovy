[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice another creature?"),
                this,
                "PN may\$ sacrifice another creature. If you don't, exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    permanent
                ),
                MagicTargetHint.None,
                "another creature to sacrifice"
            );
            if (event.isYes() && targetChoice.hasOptions(game, event.getPlayer(), permanent, false)) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent, event.getPlayer(), targetChoice));
            } else {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    }
]
