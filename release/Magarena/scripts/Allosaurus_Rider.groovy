def TARGET_GREEN_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasColor(MagicColor.Green);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };
def TWO_OTHER_GREEN_CARDS_IN_HAND = new MagicCondition() {
        public boolean accept(final MagicSource source) {
		   final MagicCard caller = (MagicCard)source;
           final MagicGame game = source.getGame();
		   final MagicPlayer player = source.getController();
		   final List<MagicCard> cards = game.filterCards(player, TARGET_GREEN_CARD_FROM_HAND);
		   int amount = 0;
		   for (final MagicCard card : cards) {
               amount = (card != caller)? amount + 1 : amount;
           }
		   return amount >= 2;
        }
    };
	
[
    new MagicCardActivation(
		[TWO_OTHER_GREEN_CARDS_IN_HAND, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice1 = new MagicTargetChoice(
                new MagicTargetFilter.MagicOtherCardTargetFilter(
                    TARGET_GREEN_CARD_FROM_HAND, 
                    source
                ),
                MagicTargetHint.None,
                "a green card from your hand"
            ); 
			final MagicTargetChoice targetChoice2 = new MagicTargetChoice(
                new MagicTargetFilter.MagicOtherCardTargetFilter(
                    TARGET_GREEN_CARD_FROM_HAND, 
                    source
                ),
                MagicTargetHint.None,
                "a green card from your hand"
            );
            return [
                new MagicExileCardEvent(source, targetChoice1),
                new MagicExileCardEvent(source, targetChoice2)
            ];
        }
    },
	new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = player.getNrOfPermanents(MagicType.Land);
            pt.add(size, size);
        }
    }
]
