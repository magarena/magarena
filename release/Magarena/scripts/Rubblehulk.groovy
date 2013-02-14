[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = game.filterPermanents(player,MagicTargetFilter.TARGET_LAND_YOU_CONTROL).size();
            pt.set(size, size);
        }
    },
    new MagicBloodrushActivation(MagicManaCost.ONE_RED_GREEN) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target attacking creature\$ gets +X/+X until end of turn, where X is the number of lands you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            final int size = game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_LAND_YOU_CONTROL).size();
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,size,size));
                }
            });
        }
    }
]
