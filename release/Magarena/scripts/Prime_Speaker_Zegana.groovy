[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            int amount = 0;
            CREATURE_YOU_CONTROL.except(permanent).filter(permanent) each {
                amount = Math.max(amount,it.getPower());
            }
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,amount));
            game.logAppendMessage(permanent.getController(),permanent.getName()+" enters the battlefield with ("+amount+") +1/+1 counters on it.");
            return new MagicEvent(
                permanent,
                this,
                "PN draws cards equal to SN's power"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getPower();
            game.logAppendMessage(event.getPlayer(),"("+amount+")");
            game.doAction(new DrawAction(event.getPlayer(), amount));
        }
    }
]
