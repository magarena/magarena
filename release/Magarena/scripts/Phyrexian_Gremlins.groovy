def tappedAbility = {
    final MagicTargetFilter<MagicPermanent> filter ->
    new MagicStatic(MagicLayer.Ability, filter) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.DoesNotUntap, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            if (source.isUntapped()) {
                //remove this static after the update
                game.addDelayedAction(new MagicRemoveStaticAction(source, this));
                return false;
            } else {
                return true;
            }
        }
    };
}

def choice = Negative("target artifact");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicTapTargetPicker.Tap,
                this,
                "Target artifact\$ doesn't untap during its controller's untap step for as long as SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(it);
                game.doAction(new MagicTapAction(it));
                game.doAction(new MagicAddStaticAction(source, tappedAbility(filter)));
            });
        }
    }
]
