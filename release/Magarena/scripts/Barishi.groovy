[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN, then shuffle all creature cards from PN's graveyard into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicRemoveCardAction(permanent.getCard(),MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(permanent.getCard(),MagicLocationType.Graveyard,MagicLocationType.Exile));
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets = game.filterCards(player,MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD);
            for (final MagicCard card:targets) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
            }
        }
    }
]
