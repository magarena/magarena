[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return MagicConditionFactory.HandAtLeast(1) ? 
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Put a verse counter on SN?"),
                    this,
                    "PN may\$ put a verse counter on SN."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Verse,1));
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays X, where X is the number of verse counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
             event.processTargetCardOnStack(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.Verse);
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create("{"+amount+"}")));
            });
        }
    }
]
