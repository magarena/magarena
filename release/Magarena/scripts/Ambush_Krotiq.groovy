[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN returns another creature PN controls to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            new MagicBounceChosenPermanentEvent(
                event.getSource(), 
                event.getPlayer(),
                PosOther("target creature you control", event.getPermanent())
            );
        }
    }
]
