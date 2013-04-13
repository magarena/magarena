[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return equippedCreature.isValid() ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        equippedCreature,
                        this,
                        "Equipped creature gets +2/+2 until end of turn.") :
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
