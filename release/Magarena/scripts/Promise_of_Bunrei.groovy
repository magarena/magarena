
[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN. If PN does, he or she creates four 1/1 colorless Spirit creature tokens."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=event.getPlayer();
            if (player.controlsPermanent(permanent)) {
                game.doAction(new SacrificeAction(permanent));
                game.doAction(new PlayTokensAction(
                    player,
                    CardDefinitions.getToken("1/1 colorless Spirit creature token"),
                    4
                ));
            }
        }
    }
]
