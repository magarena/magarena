def ST1 = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.loseAllAbilities();
        permanent.addAbility(MagicTapManaActivation.Black);
    }
};

def ST2 = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Swamp);
    }
};

def UNTAPPED_ZOMBIE_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Zombie) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def AN_UNTAPPED_ZOMBIE_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_ZOMBIE_YOU_CONTROL,"an untapped Zombie you control");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Swamp"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_ZOMBIE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND,
                this,
                "Target land\$ becomes a Swamp until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddStaticAction(it, ST1));
                game.doAction(new MagicAddStaticAction(it, ST2));
            });
        }
    }
]
