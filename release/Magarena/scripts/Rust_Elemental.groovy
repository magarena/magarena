def choice = new MagicTargetChoice("another artifact you control");

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice an artifact other than SN. If PN can't, tap SN and he or she loses 4 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,choice)
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new TapAction(permanent));
                game.doAction(new ChangeLifeAction(player,-4));
            }
        }
    }
]
