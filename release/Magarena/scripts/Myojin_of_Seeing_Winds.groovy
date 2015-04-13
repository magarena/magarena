def PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player);
    } 
};
[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (permanent.hasState(MagicPermanentState.CastFromHand)) {
                game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.Divinity, 1));
            } 
            return MagicEvent.NONE;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Divinity,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw a card for each permanent you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(
                event.getPlayer(),
                event.getPlayer().getNrOfPermanents(PERMANENT_YOU_CONTROL)
            ));
        }
    }
]
