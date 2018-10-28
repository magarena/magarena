def choice = new MagicTargetChoice("another target creature");

def TypeStatic = new MagicStatic(MagicLayer.Type) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags | MagicType.Enchantment.getMask();
    }
};

def Enchantment = {
    final MagicPermanent perm ->
    final MagicGame game = perm.getGame();
    game.doAction(new AddStaticAction(perm, TypeStatic));
}

[
    new ThisBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent untapped) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{1}{R}")),
                    choice,
                ),
                this,
                "PN may\$ pay {1}{R}\$. If PN does, PN creates a token that's a copy of another target creature\$, " +
                "except it's an enchantment in addition to its other types. It gains haste. Exile it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        it,
                        Enchantment,
                        MagicPlayMod.HASTE,
                        MagicPlayMod.EXILE_AT_END_OF_TURN
                    ));
                });
            }
        }
    }
]

