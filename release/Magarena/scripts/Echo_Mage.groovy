[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=4) {
                pt.set(2,5);
            } else if (charges>=2) {
                pt.set(2,4);
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ChargeCountersAtLeast(2),
        ],
        new MagicActivationHints(MagicTiming.Spell),
        "Copy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{U}{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
            final String description = amount == 2 ?
                    "Copy target instant or sorcery spell\$ twice. You may choose new targets for the copies.":
                    "Copy target instant or sorcery spell\$. You may choose new targets for the copy.";
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                amount,
                this,
                description
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    final MagicPlayer player = event.getPlayer();
                    final int amount=event.getRefInt();
                    for (int count=amount;count>0;count--) {
                        game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
                    }
                }
            });
        }
    }
]
