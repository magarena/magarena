def SHRINES_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.hasSubType(MagicSubType.Shrine);
        }
    };

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (permanent.getController() == upkeepPlayer) ? new MagicEvent(
                permanent,
                permanent.getController(),
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(game.filterPermanents(permanent.getController(),SHRINES_YOU_CONTROL).size()),
                this,
                "SN deals damage to target creature or player\$ equal to the number of Shrines PN controls."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int size = game.filterPermanents(event.getPlayer(),SHRINES_YOU_CONTROL).size();
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,size);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
