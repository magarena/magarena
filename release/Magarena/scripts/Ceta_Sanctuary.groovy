[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.Red) || 
                upkeepPlayer.controlsPermanent(MagicColor.Green) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card, then discards a card. If PN controls a red permanent "+
                    "and a green permanent, he or she draws two cards, then discards a card instead."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.controlsPermanent(MagicColor.Red) && player.controlsPermanent(MagicColor.Green)) {
                game.doAction(new DrawAction(event.getPlayer(), 2));
                game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer()));
            } else if (player.controlsPermanent(MagicColor.Red) || player.controlsPermanent(MagicColor.Green)) {
                game.doAction(new DrawAction(event.getPlayer()));
                game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer()));
            }
        }
    }
]
