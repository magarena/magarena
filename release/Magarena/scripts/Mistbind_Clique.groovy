[
    new MagicTrigger<ExileLinkAction>() {
        @Override
        public MagicTriggerType getType() {
            return MagicTriggerType.WhenChampioned;
        }
         
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ExileLinkAction action) {
            return action.source == permanent && action.permanent.hasSubType(MagicSubType.Faerie) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    this,
                    "Tap all lands target player\$ controls."
                ):
                MagicEvent.NONE;
        }

         @Override
         public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                LAND_YOU_CONTROL.filter(it) each {
                    final MagicPermanent land ->
                    game.doAction(new TapAction(land));
                }
            });
        }
    }
]
