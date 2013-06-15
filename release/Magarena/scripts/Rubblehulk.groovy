[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player,MagicTargetFilter.TARGET_LAND_YOU_CONTROL).size();
            pt.set(size, size);
        }
    },
    new MagicBloodrushActivation(
        MagicManaCost.create("{1}{R}{G}"),
        "Target attacking creature\$ gets +X/+X until end of turn, where X is the number of lands you control."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int size = game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_LAND_YOU_CONTROL).size();
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,size,size));
                }
            });
        }
    }
]
