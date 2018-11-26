def POWER_LESS_THAN_SAGE = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        final MagicPermanent Sage = ((MagicPermanent)source);
        final int amount = Sage.getPower();
        return target.hasType(MagicType.Creature) && target.getPower() < amount;
    }
}

def TARGET_POWER_LESS_THAN_SAGE = new MagicTargetChoice(
        POWER_LESS_THAN_SAGE,
        MagicTargetHint.Negative,
        "target creature with power less than Sage-Eye Avengers's power"
    );

def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetPermanent(game, {
            if (it.getPower() < event.getPermanent().getPower()) {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            }
        });
    }
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Choose target creature?", TARGET_POWER_LESS_THAN_SAGE),
                action,
                "PN may\$ return target creature\$ to its owner's hand if its power is less than SN's power."
            );
        }
    }
]
