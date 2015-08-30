[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN\$ draws two cards. If PN controls no artifacts, PN discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player, 2));
            if (player.getNrOfPermanents(MagicType.Artifact) == 0) {
                game.addEvent(new MagicDiscardEvent(event.getSource(), player));
            }
        }
    }
]

