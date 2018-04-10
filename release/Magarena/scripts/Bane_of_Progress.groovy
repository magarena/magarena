[
    new EntersBattlefieldTrigger() {
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
            final DestroyAction destroy = new DestroyAction(ARTIFACT_OR_ENCHANTMENT.filter(event));
            game.doAction(destroy);
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                event.getPermanent(),
                MagicCounterType.PlusOne,
                destroy.getNumDestroyed()
            ));
        }
    }
]
