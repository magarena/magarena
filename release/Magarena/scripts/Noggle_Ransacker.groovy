[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent,  final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Each player draws two cards, then discards a card at random."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player,2))
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(MagicDiscardEvent.Random(event.getSource(),player));
            }
        }
    }
]
