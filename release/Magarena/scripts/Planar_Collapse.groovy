[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return game.getNrOfPermanents(MagicType.Creature) >= 4 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices SN and destroys all creatures. They can't be regenerated."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
            CREATURE.filter(event) each {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
            }
        }
    }
]
