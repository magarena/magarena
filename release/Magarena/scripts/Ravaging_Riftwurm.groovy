[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {    
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.Charge,
                    3,
                    true
                ));
            }
            return MagicEvent.NONE;
        }
    }
]
