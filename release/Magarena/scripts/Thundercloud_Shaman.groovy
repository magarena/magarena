def NON_GIANT_CREATURE = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasType(MagicType.Creature) && target.hasSubType(MagicSubType.Giant) == false;
    }
};

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals damage equal to the number of Giants PN controls to each non-Giant creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Giant);
            game.logAppendValue(player, amount);
            NON_GIANT_CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(event.getPermanent(), it, amount));
            }
        }
    }
]
