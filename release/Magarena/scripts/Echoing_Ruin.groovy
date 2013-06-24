[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target artifact\$ and all other artifacts " +
                "with the same name as that artifact."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter<MagicPermanent> targetFilter =
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicPermanent> targets =
                        game.filterPermanents(event.getPlayer(),targetFilter);
                    for (final MagicPermanent permanent : targets) {
                        if (permanent.isArtifact()) {
                            game.doAction(new MagicDestroyAction(permanent));
                        }
                    }
                }
            });
        }
    }
]
