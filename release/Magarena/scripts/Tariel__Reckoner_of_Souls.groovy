[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Resurrect"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Return a creature card at random from your opponent's graveyard to the battlefield under your control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            
            final MagicPlayer player = event.getPlayer();
			final MagicPlayer opponent = player.getOpponent();
            final List<MagicCard> targets = game.filterCards(opponent,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            final MagicRandom rng = new MagicRandom(player.getGraveyard().getStateId());
            int actualAmount = Math.min(targets.size(),1);
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
