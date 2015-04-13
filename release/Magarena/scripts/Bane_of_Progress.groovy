[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) { 
            return new MagicEvent(
                permanent,
                this,
                "Destroy all artifacts and enchantments. " +
                "Put a +1/+1 counter on SN for each permanent destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = 
                game.filterPermanents(player,ENCHANTMENT);
            targets.addAll(game.filterPermanents(player,ARTIFACT));
            final MagicDestroyAction destroy = new MagicDestroyAction(targets);
            game.doAction(destroy);
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,destroy.getNumDestroyed()));
        }
    }
]
