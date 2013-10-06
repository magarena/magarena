[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN, then return two creature cards at random from your graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicRemoveCardAction(permanent.getCard(),MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(permanent.getCard(),MagicLocationType.Graveyard,MagicLocationType.Exile));
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets = game.filterCards(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            final MagicRandom rng = new MagicRandom(player.getGraveyard().getStateId());
            int actualAmount = Math.min(targets.size(),2);
            for (;actualAmount>0;actualAmount--) {
                final int index = rng.nextInt(targets.size());
                final MagicCard card = targets.get(index);
                game.doAction(new MagicReanimateAction(
                    card,
                    player
                ));
            }
        }
    }
]
