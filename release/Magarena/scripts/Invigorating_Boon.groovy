[
    new MagicWhenOtherCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.POS_TARGET_CREATURE),
                MagicPumpTargetPicker.create(),
                this,
                ""+permanent.getController()+" may\$ put a +1/+1 counter on target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,{
                    game.doAction(new MagicChangeCountersAction(it,MagicCounterType.PlusOne,1));
                });
            }
        }
    }
]
