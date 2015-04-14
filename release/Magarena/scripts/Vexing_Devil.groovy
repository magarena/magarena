[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(),
                this,
                "PN may\$ have SN deal 4 damage to him or her. If a he or she does, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),4));
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            } 
        }
    }
]
