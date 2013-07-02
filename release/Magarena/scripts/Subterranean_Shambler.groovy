def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicSource source = event.getSource();
    final Collection<MagicPermanent> creatures =
        game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
    for (final MagicPermanent creature : creatures) {
        final MagicDamage damage = new MagicDamage(source,creature,1);
        game.doAction(new MagicDealDamageAction(damage));
    }
} as MagicEventAction

def genEvent = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "SN deals 1 damage to each creature without flying."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return genEvent(permanent);
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) ?
                genEvent(permanent) : MagicEvent.NONE;
        }
    }
]
