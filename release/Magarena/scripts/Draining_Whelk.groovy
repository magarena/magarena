[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_SPELL,
                this,
                "Counter target spell\$. Put X +1/+1 counters on SN, " +
                "where X is that spell's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it));
                game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(),MagicCounterType.PlusOne,it.getConvertedCost()));
            });
        }
    }
]
