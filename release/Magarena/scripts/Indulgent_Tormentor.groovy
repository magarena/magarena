def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isMode(1)) {
        game.doAction(new DrawAction(event.getRefPlayer(),1));
    } else if (event.isMode(2)) {
        final MagicEvent sac = new MagicSacrificePermanentEvent(
        event.getPlayer(),
        SACRIFICE_CREATURE
        );
    if (sac.isSatisfied()) {
        game.addEvent(sac);
    } else {
        game.doAction(new DrawAction(event.getRefPlayer(),1));
    }

     } else if (event.isMode(3)) {
    game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
     } else {
    game.doAction(new DrawAction(event.getRefPlayer(),1));

    }
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                TARGET_OPPONENT,
                this,
                "Target opponent\$ choose one — (1) Have PN draw a card; "+
                "or (2) "+permanent.getOpponent()+" sacrifices a creature; ."+
                "or (3) "+permanent.getOpponent()+" pays 3 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicOrChoice(
                        MagicChoice.NONE,
                        MagicChoice.NONE,
                        MagicChoice.NONE
                    ),
                    event.getPlayer(),
                    action,
                    "\$"
                ));
            });
        }
    }
]
