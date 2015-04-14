[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN, then return two creature cards at random from your graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new RemoveCardAction(permanent.getCard(),MagicLocationType.Graveyard));
            game.doAction(new MoveCardAction(permanent.getCard(),MagicLocationType.Graveyard,MagicLocationType.Exile));

            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = new MagicCardList(game.filterCards(player,CREATURE_CARD_FROM_GRAVEYARD));
            for (final MagicCard card : cards.getRandomCards(2)) {
                game.doAction(new ReanimateAction(
                    card,
                    player
                ));
            }
        }
    }
]
