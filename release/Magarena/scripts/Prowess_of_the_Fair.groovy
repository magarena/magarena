[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.hasSubType(MagicSubType.Elf) &&
                    otherPermanent.isOwner(permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ put a 1/1 green Elf Warrior creature token onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 green Elf Warrior creature token")
                ));
            }
        }
    }
]
