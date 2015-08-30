[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN sacrifices a permanent for each soot counter on SN."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Soot);
            game.logAppendValue(event.getPlayer(), amount);
            if (amount > 0) {
                for (int i=amount;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        event.getPlayer(),
                        SACRIFICE_PERMANENT
                    ));
                }     
            }
        }
    }
]
//Would prefer this to occur before the scripted adding of a token
