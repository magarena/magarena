def CREATURE_OR_LAND_TO_SACRIFICE = new MagicTargetChoice(
    CREATURE_OR_LAND_YOU_CONTROL,
    MagicTargetHint.None,
    "a creature or land to sacrifice"
)    


[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "PN sacrifices a creature or land."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getPermanent(),
                event.getPlayer(),
                CREATURE_OR_LAND_TO_SACRIFICE
            ));
        }
    }
]
