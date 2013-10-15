def DiesUntap = new MagicWhenOtherDiesTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
        return otherPermanent.isCreature() ?
            new MagicEvent(
                permanent,
                this,
                "Untap SN."
            ) :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicUntapAction(event.getPermanent()));
    }
};

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(DiesUntap);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) { 
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Shaman)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent,
                    this,
                    "You may\$ attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicAttachAction(
                    event.getPermanent(),
                    event.getRefPermanent()
                ));
            }
        }
    }
]
