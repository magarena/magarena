def filter = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && target.hasAbility(MagicAbility.Flying) == false && target.hasColor(MagicColor.Blue) == false;
    }
};

[
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals damage equal to half the number of Islands PN controls, rounded down, "+
                "to each nonblue creature without flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            def amount = player.getNrOfPermanents(MagicSubType.Island).intdiv(2);
            game.logAppendValue(player,amount);
            filter.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
        }
    }
]
