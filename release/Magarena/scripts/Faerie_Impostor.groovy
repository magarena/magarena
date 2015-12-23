def choice = new MagicTargetChoice("another creature you control");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ return another creature he or she controls to its owner's hand. " +
                "If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent bounce = new MagicBounceChosenPermanentEvent(
                event.getSource(),
                event.getPlayer(),
                choice
            );
            if (event.isYes() && bounce.isSatisfied()) {
                game.addEvent(bounce);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
