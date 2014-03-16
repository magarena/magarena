def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer opponent=event.getPlayer().getOpponent();
    if (opponent.controlsPermanent(MagicType.Creature)) {
        game.addEvent(new MagicSacrificePermanentEvent(
            event.getPermanent(),
            opponent,
            MagicTargetChoice.SACRIFICE_CREATURE
        ));
    }
}

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN's opponent sacrifices a creature."
    );
}

[
    new MagicHeroicTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack spell) {
            return event(permanent);
        }
    }
]
