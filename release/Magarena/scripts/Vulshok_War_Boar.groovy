[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice an artifact?"),
                this,
                "PN may\$ sacrifice an artifact. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().controlsPermanent(MagicType.Artifact) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_ARTIFACT));
            } else {
                game.doAction(new SacrificeAction(perm));
            }
        }
    }
]
