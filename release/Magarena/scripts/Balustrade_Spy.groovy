// When SN enters the battlefield, target player reveals cards from the top of his or her library until he or she reveals a land card, then puts those cards into his or her graveyard.

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ reveals cards from the top of his or her library until he or she reveals a land card, " +
                "then puts all cards revealed this way into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList library = it.getLibrary();
                int landCards = 0;
                while (landCards < 1 && library.size() > 0) {
                    if (library.getCardAtTop().hasType(MagicType.Land)) {
                        landCards++;
                    }
                    game.doAction(new MillLibraryAction(it,1));
                }
            });
        }
    }
]
