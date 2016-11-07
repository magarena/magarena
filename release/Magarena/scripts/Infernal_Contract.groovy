[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws four cards. PN loses half his or her life, rounded up."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),4));
            game.doAction(new ChangeLifeAction(event.getPlayer(), -event.getPlayer().getHalfLifeRoundUp()));
        }
    }
]
