[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Draw four cards. You lose half your life, rounded up."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),4));
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                -(int)Math.ceil(event.getPlayer().getLife()/2)
            ));
        }
    }
]
