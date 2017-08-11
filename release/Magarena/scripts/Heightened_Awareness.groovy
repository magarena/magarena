[
    new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put SN onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardHandEvent(event.getSource(), event.getPlayer()));
            game.doAction(new PlayCardFromStackAction(event.getCardOnStack()));
        }
    }
]
