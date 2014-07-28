[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{B}{B}")];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(-1,-1),
                this,
                "Target creature\$ gets -1/-1 until end of turn. Put a +1/+1 counter on SN."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,-1,-1));
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
            });
        }
    }
]
