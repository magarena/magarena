[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) && card.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),1));
        }
    }
]
