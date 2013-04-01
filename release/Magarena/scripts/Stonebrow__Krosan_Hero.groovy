[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (creature.getController() == player && 
                creature.hasAbility(MagicAbility.Trample)) ?
                new MagicEvent(
                    permanent,
                    player,
                    creature,
                    this,
                    creature + " gets +2/+2 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getRefPermanent(),2,2));
        }
    }
]
