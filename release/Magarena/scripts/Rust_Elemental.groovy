[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice an artifact other than SN. If you can't, tap SN and lose 4 life."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.ARTIFACT_YOU_CONTROL,
                    permanent
                ),
                MagicTargetHint.None,
                "an artifact other than " + permanent + " to sacrifice"
            );
		if (player.getNrOfPermanents(MagicType.Artifact) >=2) {
            game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
		} else {
			game.doAction(new MagicTapAction(event.getPermanent(),true));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-4));		
		}
        }
    }
]
