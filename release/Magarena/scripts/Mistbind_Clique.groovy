[
    new MagicTrigger<MagicExileLinkAction>() {
        @Override
        public MagicTriggerType getType() {
            return MagicTriggerType.WhenChampioned;
        }
         
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicExileLinkAction action) {
            return action.source == permanent && action.permanent.hasSubType(MagicSubType.Faerie) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "Tap all lands target player\$ controls."
                ):
                MagicEvent.NONE;
        }

         @Override
         public void executeEvent(final MagicGame game, final MagicEvent event) {
             event.processTargetPlayer(game, {
                final Collection<MagicPermanent> targets = it.filterPermanents(MagicTargetFilterFactory.LAND_YOU_CONTROL);
                for (final MagicPermanent land : targets) {
                    game.doAction(new MagicTapAction(land,true));
                }
             });
        }
    }
]
