def choice = new MagicTargetChoice("a nonland permanent to sacrifice");

[
    new MagicPlaneswalkerActivation(3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getOpponent(),
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ — (1) PN lose 3 life; " +
                "or (2) PN sacrifices a nonland permanent; " +
                "or (3) PN discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.doAction(new ChangeLifeAction(event.getPlayer(), -3));
            } else if (event.isMode(2)) {
                final MagicEvent sac = new MagicSacrificePermanentEvent(
                    event.getPlayer(),
                    choice
                );
                if (sac.isSatisfied()) {
                    game.addEvent(sac);
                } else {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), -3));
                }
            } else if (event.isMode(3)) {
                final MagicEvent discard = new MagicDiscardEvent(event.getPlayer())
                if (discard.isSatisfied()) {
                    game.addEvent(discard);
                } else {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), -3));
                }
            }
        }
    }
]
