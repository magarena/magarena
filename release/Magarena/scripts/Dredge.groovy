[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN sacrifice a creature or land. PN draws a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), new MagicTargetChoice("a creature or land to sacrifice")));
            game.addEvent(new MagicDrawEvent(event.getSource(),event.getPlayer(),1));
        }
    }
]
