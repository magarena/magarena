def TARGET_ARTIFACT_YOU_CONTROL = new MagicTargetChoice("target artifact you control");

def PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(0,0);
    }
};

def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Construct);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask()|MagicType.Artifact.getMask();
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_ARTIFACT_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                payedCost.getX(),
                this,
                "PN puts X +1/+1 counters on target artifact he or she controls\$. "+
                "If it isn't a creature or Vehicle, it becomes a 0/0 Construct artifact creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount=event.getRefInt();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ChangeCountersAction(it, MagicCounterType.PlusOne, amount));
                if (!it.hasType(MagicType.Creature) && !it.hasSubType(MagicSubType.Vehicle)) {
                    game.doAction(new BecomesCreatureAction(it, PT, ST));
                }
            });
        }
    }
]
