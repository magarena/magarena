[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard your hand?"),
                this,
                "PN may\$ discard all the cards in his or her hand. If PN does, he or she draws that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final MagicEvent discard = new MagicDiscardHandEvent(player);
            final int amount = player.getHandSize();
            if (event.isYes() && discard.isSatisfied()) {
                game.logAppendValue(player, amount);
                game.addEvent(discard);
                game.addEvent(new MagicDrawEvent(event.getSource(), player, amount));
            }
        }
    }
]
