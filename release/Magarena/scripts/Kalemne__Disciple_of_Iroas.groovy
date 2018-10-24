[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return spell.isFriend(permanent) && spell.hasType(MagicType.Creature) && spell.getConvertedCost() >= 5 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gets an experience counter."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPlayer(),MagicCounterType.Experience, 1));
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = permanent.getController().getExperience();
            pt.add(amount,amount);
        }
    }
]
