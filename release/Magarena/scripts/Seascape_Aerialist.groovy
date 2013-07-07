[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PUMP,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ have Ally creatures he or she controls " +
                    "gain flying until end of turn."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final Collection<MagicPermanent> targets =
                        game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_ALLY_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Flying));
                }
            }
        }
    }
]
