def AURA_YOU_CONTROL_ATTACHED_TO_CREATURE = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Aura) && 
            target.getEnchantedPermanent().hasType(MagicType.Creature) && 
            target.isController(player);
    }
}

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = AURA_YOU_CONTROL_ATTACHED_TO_CREATURE.filter(source.getController()).size();
            pt.add(amount,amount);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent,  final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card for each Aura he or she controls that's attached to a creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = AURA_YOU_CONTROL_ATTACHED_TO_CREATURE.filter(player).size();
            game.logAppendValue(player, amount);
            game.doAction(new DrawAction(player, amount));
        }
    }
]
