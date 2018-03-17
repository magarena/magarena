def ZombieStatic = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.removeAll(MagicSubType.ALL_CREATURES);
        flags.add(MagicSubType.Zombie);
    }
}

def PT = {
    final MagicGame game, final MagicPermanent perm ->
    game.doAction(new AddStaticAction(perm, MagicStatic.genPTSetStatic(4, 4)));
}

def Black = {
    final MagicGame game, final MagicPermanent perm ->
    game.doAction(new AddStaticAction(perm, MagicStatic.IsBlack));
}

def Zombie = {
    final MagicGame game, final MagicPermanent perm ->
    game.doAction(new AddStaticAction(perm, ZombieStatic));
}

[
    new AtBeginOfCombatTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return permanent.isController(turnPlayer);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicTargetChoice("a creature card from your graveyard")),
                this,
                "PN may\$ exile a creature card from PN's graveyard\$. " +
                "If PN does, PN creates a token that's a copy of that card, except it's a 4/4 black Zombie. It gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        it,
                        PT,
                        Black,
                        Zombie,
                        MagicPlayMod.HASTE_UEOT
                    ));
                });
            }
        }
    }
]

