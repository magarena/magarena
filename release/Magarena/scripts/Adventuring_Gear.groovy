[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return equippedCreature.isValid() ?
                new MagicEvent(
                    permanent,
                    equippedCreature,
                    this,
                    "RN gets +2/+2 until end of turn."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),2,2));
        }
    }
]
