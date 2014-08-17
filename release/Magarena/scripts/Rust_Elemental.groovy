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
            final MagicTargetChoice targetChoice = MagicTargetChoice.Other("an artifact to sacrifice", permanent);
            if (targetChoice.hasOptions(game, player, permanent, false)) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
            } else {
                game.doAction(new MagicTapAction(permanent,true));
                game.doAction(new MagicChangeLifeAction(player,-4));        
            }
        }
    }
]
