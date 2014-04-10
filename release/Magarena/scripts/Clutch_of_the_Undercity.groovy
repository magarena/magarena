def public MagicCardActivation Transmute(final String transmuteCost) {
    return new MagicCardActivation([MagicCondition.SORCERY_CONDITION],new MagicActivationHints(MagicTiming.Main),"Transmute"+transmuteCost) {
        public MagicTargetChoice getTransmuteChoice(final MagicCard transmuteSource) {
            final int cmc = transmuteSource.getCardDefinition().getConvertedCost();
            final MagicCardFilterImpl transmuteFilter = new MagicCardFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                    return target.getCardDefinition().getConvertedCost() == cmc;
                }
                public boolean acceptType(final MagicTargetType targetType) {
                    return targetType == MagicTargetType.Library;
                }
            };
            return new MagicTargetChoice(transmuteFilter,"a card with converted mana cost of " + cmc);
        }
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,transmuteCost)];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                getTransmuteChoice(cardOnStack.getCard()),
                this,
                "PN searches his or her library for a card with converted mana cost of "+cardOnStack.getCardDefinition().getConvertedCost()+". "+
                "Then PN shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.logAppendMessage(event.getPlayer(), event.getPlayer().getName()+" reveals (" + card + ").");
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
                    game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                    game.doAction(new MagicRemoveCardAction(event.getCardOnStack().getCard(),MagicLocationType.Stack));
                    game.doAction(new MagicMoveCardAction(event.getCardOnStack().getCard(),MagicLocationType.Stack,MagicLocationType.Graveyard));
                }
            });
        }
    }
};

[   
    Transmute("{1}{U}{B}"),
    
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target permanent\$ to its owner's hand. Then that player loses 3 life."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                game.doAction(new MagicChangeLifeAction(permanent.getController(),-3));
            });
        }
    }
    
]
