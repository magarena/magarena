[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
            return (permanent.isFriend(spell) && spell.isInstantOrSorcerySpell()) && spell.getConvertedCost() > permanent.getController().getExperience() ?
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
    new MagicStatic(MagicLayer.CostReduction) {
        @Override
        public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
            if ((card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)) && source.isFriend(card)) {
                final int XP = source.getController().getExperience();
                return cost.reduce(XP);
            } else {
                return cost;
            }
        }
    }
]
