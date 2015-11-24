def youLoseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new SacrificeAction(event.getPermanent()));
}

def playerLoseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN flips a coin. If PN loses the flip, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicCoinFlipEvent(
                event,
                MagicEventAction.NONE,
                youLoseAct
            ));
        }
    },
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack.getController(),
                cardOnStack,
                this,
                "PN flips a coin. If PN loses the flip, counter RN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicCoinFlipEvent(
                event,
                event.getRefCardOnStack(),
                MagicEventAction.NONE,
                playerLoseAct
            ));
        }
    }
]
