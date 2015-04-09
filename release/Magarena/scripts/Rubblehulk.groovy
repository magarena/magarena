[
    new MagicCardAbilityActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Bloodrush"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{R}{G}"),
                new MagicDiscardSelfEvent(source)
            ];
        }

        @Override
        public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_ATTACKING_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target attacking creature\$ gets +X/+X until end of turn, where X is the number of lands you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int size = game.filterPermanents(event.getPlayer(),LAND_YOU_CONTROL).size();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,size,size));
            });
        }
    }
]
