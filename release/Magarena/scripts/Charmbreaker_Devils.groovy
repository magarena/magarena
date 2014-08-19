[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return an instant or sorcery card at random from your graveyard to your hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets = game.filterCards(player,MagicTargetFilterFactory.INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
            if (targets.size() > 0) {
                final MagicPermanent permanent = event.getPermanent();
                final MagicRandom rng = new MagicRandom(player.getGraveyard().getStateId());
                final int index = rng.nextInt(targets.size());
                final MagicCard card = targets.get(index);
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
