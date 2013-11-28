def CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return true;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_CARD_FROM_LIBRARY = new MagicTargetChoice(
    CARD_FROM_LIBRARY,
    "a card"
);

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicDiscardEvent(event.getPermanent(),player,1));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                A_CARD_FROM_LIBRARY,
                this,
                "PN may search his or her library for a card, shuffle his or her library, and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo() == false) {
                event.processTargetCard(game, new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
                    }
                });
            }
        }
    },
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put all creature cards from all graveyards onto the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicCard> targets = game.filterCards(
                player,
                MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS
            );
            for (final MagicCard card : targets) {
                game.doAction(new MagicReanimateAction(
                    card, 
                    player
                ));
            }
        }
    }
]
