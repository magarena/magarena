[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicBasicLandChoice.ALL_INSTANCE,
                    this,
                    "SN gains landwalk of the chosen type\$ until end of turn."
                ): 
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSubType subType = event.getChosenSubType();
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),subType.getLandwalkAbility()));
        }
    }
]
