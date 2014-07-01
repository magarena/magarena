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
            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new MagicDrawAction(player,2))
                game.addEvent(MagicDiscardEvent.Random(event.getSource(),player));
            }
        }
    }
]
