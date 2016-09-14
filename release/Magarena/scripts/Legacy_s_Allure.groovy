[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicPTTargetFilter(
                    CREATURE,
                    source.getCounters(MagicCounterType.Treasure)
                ),
                MagicTargetHint.Negative,
                "target creature to gain control of"
            );
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
            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(event.getPlayer(),it));
            });
        }
    }
]
