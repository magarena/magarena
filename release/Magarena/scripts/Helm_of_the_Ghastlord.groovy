def Draw = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        return (damage.isSource(permanent) && permanent.isOpponent(damage.getTarget())) ?
            new MagicEvent(
                permanent,
                this,
                "PN draws a card."
            ) :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new DrawAction(event.getPlayer()));
    }
};
def Discard = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        return (damage.isSource(permanent) && permanent.isOpponent(damage.getTarget())) ?
            new MagicEvent(
                permanent,
                damage.getTarget(),
                this,
                "RN discards a card."
            ) :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getRefPlayer()));
    }
};

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Blue);
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(Draw);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Blue);
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Black);
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(Discard);
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Black);
        }
    }
]
