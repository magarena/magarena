[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -1/-1 until end of turn for each basic land type among lands you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int domain = player.getDomain();
            game.logAppendMessage(player," ("+domain+")");
            CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it,-domain,-domain));
            }
        }
    }
]
