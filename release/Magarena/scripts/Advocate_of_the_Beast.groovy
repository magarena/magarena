[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ? 
                new MagicEvent(
                    permanent,
                    new MagicTargetChoice("target Beast creature you control"),
                    MagicPumpTargetPicker.create(),
                    this,
                    "PN puts a +1/+1 counter on target Beast creature\$ he or she controls."
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
