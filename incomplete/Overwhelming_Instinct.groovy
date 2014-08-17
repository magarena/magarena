[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isFriend(creature) && permanent.getController().getNrOfAttackers() >= 3 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer()));
        }
    }
]
