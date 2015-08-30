[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final MagicCardList cardList = new MagicCardList(player.getGraveyard());
            cardList.addAll(player.getOpponent().getGraveyard());
            int types = 0;
            for (final MagicType type : MagicType.ALL_CARD_TYPES) {
                for (final MagicCard card : cardList) {
                    if (card.hasType(type)) {
                        types++;
                        break;
                    }
                }
            }
            pt.set(types, types + 1);
        }
    }
]
