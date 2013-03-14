[
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEquippedCreature()==died) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
        }
    }
]
