def action = {
    final MagicGame game, final MagicEvent event -> {
        final MagicSource source = event.getSource();
        final Collection<MagicPermanent> creatures =
            game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
        for (final MagicPermanent creature : creatures) {
            final MagicDamage damage = new MagicDamage(source,creature,1);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
} as MagicEventAction

def genEvent(permanent) {
    return new MagicEvent(
        permanent,
        player,
        action,
        "SN deals 1 damage to each creature without flying."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return genEvent(permanent);
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent left) {
            return (permanent == left) ?
                genEvent(permanent) : MagicEvent.NONE;
        }
    }
]
