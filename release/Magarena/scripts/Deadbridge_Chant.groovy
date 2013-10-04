[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts the top ten cards of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),10));
        }
    }, 
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN chooses a card at random in his or her graveyard. " +
                    "If it's a creature card, put it onto the battlefield. " +
                    "Otherwise, put it into PN's hand."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets = game.filterCards(player,MagicTargetFilter.TARGET_CARD_FROM_GRAVEYARD);
            final MagicRandom rng = new MagicRandom(player.getGraveyard().getStateId());
            if (targets.isEmpty() == false) {
                final int index = rng.nextInt(targets.size());
                final MagicCard card = targets.get(index);
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new MagicReanimateAction(
                        card,
                        player
                    ));
                } else {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
