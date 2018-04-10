[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(
                permanent.getController(),
                permanent,
                MagicCounterType.Age,
                1
            ));
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay cumulative upkeep?"),
                this,
                "PN may\$ sacrifice a creature for each Age counter on SN. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicRepeatedCostEvent(
                    event.getSource(),
                    SACRIFICE_CREATURE,
                    event.getPermanent().getCounters(MagicCounterType.Age),
                    MagicChainEventFactory.Sac
                ));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
