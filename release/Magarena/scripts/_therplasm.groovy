def action = {
    final MagicGame game, final MagicEvent event ->
        if (event.isYes()) {
            event.processTargetCard(game, {
                final MagicPermanent attacker = event.getRefPermanent();
                game.doAction(new PlayCardAction(
                    it,
                    event.getPlayer(),
                    {
                        final MagicPermanent perm ->
                            final MagicGame G = perm.getGame();
                            G.doAction(new SetBlockerAction(attacker.map(G), perm));
                    }
                ));
            });
        }
}

[
    new ThisBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                permanent.getBlockedCreature(),
                this,
                "PN may\$ return SN to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent bounce = new MagicBouncePermanentEvent(event.getSource(), event.getPermanent());
            if (event.isYes() && bounce.isSatisfied()) {
                game.addEvent(bounce);
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice(A_CREATURE_CARD_FROM_HAND),
                    event.getRefPermanent(),
                    action,
                    "PN may\$ put a creature card from his or her hand\$ onto the battlefield blocking RN."
                ));
            }
        }
    }
]
