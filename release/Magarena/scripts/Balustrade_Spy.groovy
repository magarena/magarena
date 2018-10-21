// When SN enters the battlefield, target player reveals cards from the top of his or her library until he or she reveals a land card, then puts those cards into his or her graveyard.

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
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
                game.doAction(new MillLibraryUntilAction(it, MagicType.Land, 1));
            });
        }
    }
]
