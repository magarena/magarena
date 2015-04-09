[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all creatures to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE);
            for (final MagicPermanent target : targets) {
                if (!target.hasSubType(MagicSubType.Kraken) || 
                    !target.hasSubType(MagicSubType.Leviathan) ||
                    !target.hasSubType(MagicSubType.Octopus) ||
                    !target.hasSubType(MagicSubType.Serpent)) {
                    game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
