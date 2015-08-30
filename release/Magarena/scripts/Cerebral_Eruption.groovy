[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ reveals the top card of his or her library. SN deals damage "+
                "equal to the revealed card's converted mana cost to that player and each creature he or she controls. "+
                "If a land card is revealed this way, return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : it.getLibrary().getCardsFromTop(1)) {
                    game.doAction(new RevealAction(card));
                    final int amount = card.getConvertedCost();
                    game.logAppendValue(event.getPlayer(),amount);
                    game.doAction(new DealDamageAction(event.getSource(),it,amount));
                    CREATURE_YOU_CONTROL.filter(it) each {
                        game.doAction(new DealDamageAction(event.getSource(), it, amount));
                    }
                    if (card.hasType(MagicType.Land)) {
                        game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersHand));
                    }
                }
            });
        }
    }
]
