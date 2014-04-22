def MoveCards = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.BottomOfOwnersLibrary));
    });
}

[ 
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "PN reveal cards from the top of your library until he or she reveals a land card. "+
                "SN deals damage equal to the number of nonland cards revealed this way to target creature or player.\$ "+
                "If the revealed land card was a Mountain, SN deals double that damage instead. "+
                "PN puts the revealed cards on the bottom of his or her library in any order."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> library = event.getPlayer().getLibrary();
            final int size = library.size();
            final List<MagicCard> choiceList = new MagicCardList();
            event.processTarget(game, {
                final MagicTarget target ->
                int amount = 0;
                for (int i = 1; i <= size; i++) {
                    final MagicCard card = library.get(size - i);
                    game.logAppendMessage(event.getPlayer(), "Revealed ${card}. ");
                    choiceList.add(card);
                    if (card.hasType(MagicType.Land)) {
                        if (card.hasSubType(MagicSubType.Mountain)) {
                            amount = 2 * amount;
                        }
                        break;
                    } else {
                        amount++;
                    }
                }
                game.logAppendMessage(event.getPlayer(), ""+event.getSource()+" deals "+amount+" damage to "+target+". ");
                final MagicDamage damage = new MagicDamage(event.getSource(), target, amount);
                game.doAction(new MagicDealDamageAction(damage));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList, choiceList.size()),
                    MoveCards,
                    ""
                ));
            });
        }
    }
]
