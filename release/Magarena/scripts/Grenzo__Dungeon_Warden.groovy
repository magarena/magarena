[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts the bottom card of his or her library into his or her graveyard. "+
                "If it's a creature card with power less than or equal to SN's power, PN puts it onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getLibrary().size() > 0) {
                final MagicCard card = event.getPlayer().getLibrary().getCardAtBottom();
                game.doAction(new ShiftCardAction(card,MagicLocationType.BottomOfOwnersLibrary,MagicLocationType.Graveyard));
                game.logAppendMessage(event.getPlayer()," ("+card.getName()+") ");
                if (
                    card.hasType(MagicType.Creature) &&
                    (card.getPower() <= event.getPermanent().getPower())
                ) {
                    game.doAction(new ReanimateAction(card,event.getPlayer()));
                    game.logAppendMessage(event.getPlayer()," put onto the Battlefield.")
                }
            }
        }
    }
]
