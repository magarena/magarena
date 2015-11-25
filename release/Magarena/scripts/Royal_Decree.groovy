[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (tapped.hasSubType(MagicSubType.Swamp) ||
                    tapped.hasSubType(MagicSubType.Mountain) ||
                    tapped.hasColor(MagicColor.Black) ||
                    tapped.hasColor(MagicColor.Red)) ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    this,
                    "SN deals 1 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),1));
        }
    }
]
