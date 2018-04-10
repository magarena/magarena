[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(
                permanent.getController(),
                permanent,
                MagicCounterType.Wage,
                1
            ));
            final int amount = permanent.getCounters(MagicCounterType.Wage) *2;
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create(amount))),
                    this,
                    "PN may\$ pay {"+amount+"}. If he or she doesn't, remove all wage counters from SN "+
                    "and an opponent gains control of it."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isNo()) {
                game.doAction(new ChangeCountersAction(event.getPlayer(), permanent, MagicCounterType.Wage, -permanent.getCounters(MagicCounterType.Wage)));
                game.doAction(new GainControlAction(event.getPlayer().getOpponent(), permanent));
            }
        }
    }
]
