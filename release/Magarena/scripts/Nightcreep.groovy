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
            final Collection<MagicPermanent> creatures = game.filterPermanents(CREATURE);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicBecomesCreatureAction(
                    creature,
                    Color
                ));
            }
            final Collection<MagicPermanent> lands = game.filterPermanents(LAND);
            for (final MagicPermanent land : lands) {
                game.doAction(new MagicAddStaticAction(land, AB));
                game.doAction(new MagicAddStaticAction(land, ST));
            }
        }
    }
]
