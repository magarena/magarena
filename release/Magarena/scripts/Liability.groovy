[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicCard card = act.card;
            return (act.from(MagicLocationType.Battlefield) &&
                    card.isPermanent() &&
                    !card.isToken()) ?
                new MagicEvent(
                    permanent,
                    card.getOwner(),
                    this,
                    "RN loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getRefPlayer(), -1));
        }
    }
]
