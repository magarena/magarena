[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "PutCard"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "Target player\$ puts the bottom card of his or her library into his or her graveyard. "+
                "If it's a creature card, PN create a 2/2 black Zombie creature token."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
            if (it.getLibrary().size() > 0) {
                final MagicCard card = it.getLibrary().getCardAtBottom();
                game.doAction(new ShiftCardAction(card,MagicLocationType.BottomOfOwnersLibrary,MagicLocationType.Graveyard));
                game.logAppendMessage(it," ("+card.getName()+") ");
                if (
                    card.hasType(MagicType.Creature)
                ) {
                game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("2/2 black Zombie creature token"),1));
                    }
                }
            });
        }
    }
]
