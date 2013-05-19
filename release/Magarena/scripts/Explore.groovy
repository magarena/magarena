[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may play an additional land this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAddStaticAction(
                new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                        game.incMaxLand();
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return game.getTurnPlayer().getId() == event.getPlayer().getId();
                    }
                }
            ));
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
]
