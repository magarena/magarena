def BEAST_CREATURE_YOU_CONTROL = new MagicTargetFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return (target.isCreature() && target.hasSubType(MagicSubType.Beast) && player == target.getController());
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Permanent;
    }
};

def POS_TARGET_BEAST_YOU_CONTROL = new MagicTargetChoice(
    BEAST_CREATURE_YOU_CONTROL,
    MagicTargetHint.Positive,
    "target Beast creature you control"
)

[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return (eotPlayer == permanent.getController())? new MagicEvent(
                permanent,
                POS_TARGET_BEAST_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                this,
                "PN put a +1/+1 counter on target Beast creature\$ he controls."
            ):
			MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
                }
            });
        }
    }
]
