def NON_OGRE_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && target.isCreature() && target.hasSubType(MagicSubType.Ogre) == false;
    }
};

[
    new ThisLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices all non-Ogre creatures he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            NON_OGRE_CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new SacrificeAction(it));
            }
        }
    }
]
