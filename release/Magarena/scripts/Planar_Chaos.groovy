[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicCoinFlipChoice(),
                this,
                "PN flips a coin.\$ If PN loses the flip, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Boolean heads = event.isMode(1) 
            game.addEvent(new MagicCoinFlipEvent(
                event.getSource(),
                heads,
                player,
                null,
                new SacrificeAction(event.getPermanent())
            ));
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack.getController(),
                new MagicCoinFlipChoice(),
                cardOnStack,
                this,
                "PN flips a coin.\$ If PN loses the flip, counter RN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Boolean heads = event.isMode(1) 
            game.addEvent(new MagicCoinFlipEvent(
                event.getSource(),
                heads,
                player,
                null,
                new CounterItemOnStackAction(event.getRefCardOnStack())
            ));
        }
    }
]
