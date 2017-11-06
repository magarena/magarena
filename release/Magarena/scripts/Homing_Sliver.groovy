[
new MagicCDA() {
  @Override
  public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {

    final MagicCardList cardList = new MagicCardList(player.getHand());
    cardList.addAll(player.getOpponent().getHand());

    for (final MagicCard card : cardList) {
      if (card.hasType(Sliver)) {
        card.addAbility(MagicAbility.TypeCycling(Sliver));
      }
    }
  }
}
]
