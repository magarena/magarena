[
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer drawPlayer) {
            return drawPlayer == permanent.getController() ?
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy each creature with converted mana cost equal to the number of age counters on SN. "+
                    "They can't be regenerated."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Age);
            game.logAppendMessage(event.getPlayer(), "("+amount+")");
            CREATURE.filter(event) each {
                if (it.getConvertedCost() == amount) {
                    game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new DestroyAction(it));
                }
            }
        }
    }
]
