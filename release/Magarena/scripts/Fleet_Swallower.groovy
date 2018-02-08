[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top half of his or her library, rounded up, into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                def amount = (it.getLibrary().size() + 1).intdiv(2);
                game.doAction(new MillLibraryAction(it, amount));
            });
        }
    }
]

