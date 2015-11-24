[
    new ThisDamagePlayerTrigger() {     
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {    
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.DRAW_CARDS,
                    amount,
                    MagicSimpleMayChoice.DEFAULT_NONE
                ),
                this,
                "PN may\$ draw X cards, where X is the number of charge counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            final MagicPlayer player = event.getPlayer();
            game.logAppendX(player, amount);
            if (event.isYes()) {
                game.doAction(new DrawAction(player, amount));
            }
        }
    }
]
