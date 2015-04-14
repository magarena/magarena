[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws a card for each Bird on the battlefield."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Bird) + event.getPlayer().getOpponent().getNrOfPermanents(MagicSubType.Bird);
            game.doAction(new DrawAction(event.getPlayer(),amount));
        }
    }
]
