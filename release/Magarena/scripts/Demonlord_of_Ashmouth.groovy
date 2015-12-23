[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice another creature?"),
                this,
                "PN may\$ sacrifice another creature. If PN doesn't, exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                permanent,
                player,
                MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL
            );
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new RemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    }
]
