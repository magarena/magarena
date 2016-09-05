[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanentList CreatureList = new MagicPermanentList();
            CreatureList.add(died.getBlockedCreature());
            CreatureList.addAll(died.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                CreatureList,
                this,
                "Destroy all creatures blocking or blocked by SN. They can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList destroyed = event.getRefPermanentList();
            while(destroyed.remove(MagicPermanent.NONE));
            if (destroyed.size() > 0) {
                for (final MagicPermanent creature : destroyed) {
                    game.doAction(ChangeStateAction.Set(creature, MagicPermanentState.CannotBeRegenerated));
                }
                game.doAction(new DestroyAction(destroyed));
            }
        }
    }
]
