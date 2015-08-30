[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top four cards of his or her library into his or her graveyard. PN draws a card for each creature card put into that graveyard this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MillLibraryAction millAct = new MillLibraryAction(it, 4);
                game.doAction(millAct);
                int amount = 0;
                for (final MagicCard card : millAct.getMilledCards()) {
                    if (card.hasType(MagicType.Creature) && card.isInGraveyard()) {
                        amount++;
                    }
                }
                game.doAction(new DrawAction(event.getPlayer(), amount));
            });
        }
    }
]
