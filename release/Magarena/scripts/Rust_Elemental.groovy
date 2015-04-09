[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice an artifact other than SN. If you can't, tap SN and lose 4 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicTargetChoice targetChoice = Other("an artifact to sacrifice", permanent);
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,targetChoice)
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicTapAction(permanent));
                game.doAction(new MagicChangeLifeAction(player,-4));        
            }
        }
    }
]
