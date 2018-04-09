[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(permanent.getController(), permanent, MagicCounterType.PlusOne, 1));
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create(amount))
                ),
                amount,
                this,
                "PN may\$ pay {X}, where X is the number of +1/+1 counters on it. If PN doesn't, "+
                "tap SN and it deals X damage to him or her. (X=${amount})"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicPermanent permanent = event.getPermanent();
                game.doAction(new TapAction(permanent));
                game.doAction(new DealDamageAction(permanent, permanent.getController(), event.getRefInt()));
            }
        }
    }
]
