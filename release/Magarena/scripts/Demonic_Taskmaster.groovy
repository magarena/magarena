[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a creature other than SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                permanent
            );
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter,
                false,
                MagicTargetHint.None,
                "a creature other than " + permanent + " to sacrifice"
            );
            game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
        }
    }
]
