[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            int amount = 0;
            CREATURE_YOU_CONTROL.except(permanent).filter(permanent) each {
                amount = Math.max(amount,it.getPower());
            }
            game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,amount));
            game.logAppendMessage(permanent.getController(),permanent.getName()+" enters the battlefield with ("+amount+") +1/+1 counters on it.");
            return MagicEvent.NONE;
        }
    }
]
