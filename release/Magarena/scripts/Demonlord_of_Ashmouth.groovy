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
            final MagicPlayer player = event.getPlayer();
            final MagicTargetChoice targetChoice = Other("a creature to sacrifice", permanent);
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,targetChoice)
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new RemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    }
]
