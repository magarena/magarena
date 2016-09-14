[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level=permanent.getCounters(MagicCounterType.Level);
            if (level>=4) {
                pt.set(2,5);
            } else if (level>=2) {
                pt.set(2,4);
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.CounterAtLeast(MagicCounterType.Level,2),
        ],
        new MagicActivationHints(MagicTiming.Spell),
        "Copy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{U}{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=source.getCounters(MagicCounterType.Level)>=4?2:1;
            final String description = amount == 2 ?
                    "Copy target instant or sorcery spell\$ twice. PN may choose new targets for the copies.":
                    "Copy target instant or sorcery spell\$. PN may choose new targets for the copy.";
            return new MagicEvent(
                source,
                TARGET_INSTANT_OR_SORCERY_SPELL,
                amount,
                this,
                description
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount=event.getRefInt();
                for (int count=amount;count>0;count--) {
                    game.doAction(new CopyCardOnStackAction(player,it));
                }
            });
        }
    }
]
