[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice("Sacrifice a land?"),
                this,
                "PN may\$ sacrifice a land. If a he or she does, put SN on the top of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().controlsPermanent(MagicType.Land) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_LAND));
                game.doAction(new RemoveFromPlayAction(
                    perm,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            }
        }
    }
]
