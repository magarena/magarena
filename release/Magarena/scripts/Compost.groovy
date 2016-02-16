def effect = MagicRuleEventAction.create("You may draw a card.");

[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) && card.hasColor(MagicColor.Black)) ?
                effect.getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
