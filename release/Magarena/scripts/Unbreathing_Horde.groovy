[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            final int amount = game.filterPermanents(player,ZOMBIE_YOU_CONTROL.except(permanent)).size() +
                               game.filterCards(player,ZOMBIE_CARD_FROM_GRAVEYARD).size();
            game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    },
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.getTarget() == permanent) {

                // Prevention effect.
                damage.prevent();

                return new MagicEvent(
                    permanent,
                    this,
                    "Remove a +1/+1 counter from SN."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-1));
        }
    }
]
