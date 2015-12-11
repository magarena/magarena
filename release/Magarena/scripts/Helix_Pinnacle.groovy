[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getCounters(MagicCounterType.Tower) >= 100 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN wins the game."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().getCounters(MagicCounterType.Tower) >= 100) {
                game.doAction(new LoseGameAction(event.getPlayer().getOpponent()));
            }
        }
    },
    
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "PN puts RN tower counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.Tower, event.getRefInt()));
        }
    }
]
