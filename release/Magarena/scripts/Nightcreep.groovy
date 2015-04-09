def Color = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.Black.getMask();
    }
};

def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.loseAllAbilities();
        permanent.addAbility(MagicTapManaActivation.Black);
    }
};

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Swamp);
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, all creatures become black and all lands become Swamps."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(game) each {
                game.doAction(new MagicAddStaticAction(it, Color));
            }
            LAND.filter(game) each {
                game.doAction(new MagicAddStaticAction(it, AB));
                game.doAction(new MagicAddStaticAction(it, ST));
            }
        }
    }
]
