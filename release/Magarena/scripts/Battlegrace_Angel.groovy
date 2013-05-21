[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (creature.getController()==player&&player.getNrOfAttackers()==1)?
                new MagicEvent(
                    permanent,
                    player,
                    creature,
                    this,
                    "RN gains lifelink until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicSetAbilityAction(event.getRefPermanent(),MagicAbility.Lifelink));
        }
    }
]
