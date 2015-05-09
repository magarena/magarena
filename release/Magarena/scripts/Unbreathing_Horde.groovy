[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            final int amount = game.filterPermanents(player,ZOMBIE_YOU_CONTROL.except(permanent)).size() +
                               ZOMBIE_CARD_FROM_GRAVEYARD.filter(player).size();
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    },
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.getTarget() == permanent) {
                damage.prevent();
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.PlusOne,-1));
            }
            return MagicEvent.NONE;
        }
    }
]
