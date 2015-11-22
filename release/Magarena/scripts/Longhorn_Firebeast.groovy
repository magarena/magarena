[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(),
                this,
                "PN may\$ have SN deal 5 damage to him or her. If he or she does, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),5));
                game.doAction(new SacrificeAction(event.getPermanent()));
            } 
        }
    }
]
