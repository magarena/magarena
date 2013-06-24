[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ put a treasure counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter =
                    new MagicTargetFilter.MagicPowerTargetFilter(
                        MagicTargetFilter.TARGET_CREATURE,
                        source.getCounters(MagicCounterType.Charge)
                    );
            final MagicTargetChoice targetChoice =
                    new MagicTargetChoice(
                    targetFilter,true,MagicTargetHint.Negative,"target creature to gain control of");
            return new MagicEvent(
                source,
                targetChoice,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$ with power less than or " +
                "equal to the number of treasure counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainControlAction(event.getPlayer(),creature));
                }
            });
        }
    }
]
