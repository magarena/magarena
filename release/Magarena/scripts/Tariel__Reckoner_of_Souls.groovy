[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Return a creature card at random from target opponent's\$ graveyard to the battlefield under your control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer opponent ->
                final List<MagicCard> targets = game.filterCards(opponent,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
                final MagicRandom rng = new MagicRandom(opponent.getGraveyard().getStateId());
                if (targets.size() > 0)
                    final int index = rng.nextInt(targets.size());
                    final MagicCard card = targets.get(index);
                    game.doAction(new MagicReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                }
            } as MagicPlayerAction);
        }
    }
]
