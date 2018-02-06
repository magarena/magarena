[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws three cards, then PN discards two cards at random. " +
                "If two cards that share a card type are discarded this way, put two +1/+1 counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), 3));
            final MagicCardList hand = new MagicCardList(event.getPlayer().getHand());
            final MagicCardList toDiscard = hand.getRandomCards(2);

            toDiscard.each {
                game.doAction(new DiscardAction(event.getPlayer(), it));
            }

            if (MagicType.ALL_CARD_TYPES.any({
                final MagicType type -> toDiscard.every({ it.hasType(type) })
            })) {

                game.doAction(new ChangeCountersAction(event.getSource(), MagicCounterType.PlusOne, 2));
            }
        }
    }
]

