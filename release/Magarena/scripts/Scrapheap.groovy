[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return ((died.hasType(MagicType.Artifact) || died.hasType(MagicType.Enchantment)) &&
                    died.getOwner() == permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), 1));
        }
    }
]
