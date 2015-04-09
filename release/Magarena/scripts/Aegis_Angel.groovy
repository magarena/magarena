def control = {
    final int you, final MagicTargetFilter<MagicPermanent> filter ->
    return new MagicStatic(MagicLayer.Ability,filter) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Indestructible, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            if (you != source.getController().getIndex()) {
                //remove this static after the update
                game.addDelayedAction(new MagicRemoveStaticAction(source, this));
                return false;
            } else {
                return true;
            }
        }
    };
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                PosOther("target permanent",permanent),
                MagicIndestructibleTargetPicker.create(),
                this,
                "Another target permanent\$ gains indestructible for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(it);
                game.doAction(new MagicAddStaticAction(source, control(source.getController().getIndex(), filter)));
            });
        }
    }
]
