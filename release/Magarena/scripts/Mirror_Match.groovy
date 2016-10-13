def blocking = {
    final MagicPermanent attacker ->
    return {
        final MagicPermanent perm ->
        final MagicGame game = perm.getGame();
        game.doAction(new SetBlockerAction(attacker.map(game), perm));
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each creature attacking PN or a planeswalker he or she controls, "+
                "PN creates a token that's a copy of that creature and that's blocking that creature. "+
                "Exile those tokens at end of combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent creature : ATTACKING_CREATURE_OPP_CONTROL.filter(event)) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    creature,
                    MagicPlayMod.EXILE_AT_END_OF_COMBAT,
                    blocking(creature)
                ));
            }
        }
    }
]
