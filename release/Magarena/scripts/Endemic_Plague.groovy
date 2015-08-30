[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "Destroy all creatures that share a creature type with RN. They can't be regenerated."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent sacrificed=event.getRefPermanent();
            final List<MagicPermanent> creatures = new ArrayList();
            for (final MagicSubType subType : MagicSubType.values()) {
                if (sacrificed.hasSubType(subType)){
                    MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.creature(subType,Control.Any);
                    creatures.addAll(filter.filter(event));
                }
            }
            for (final MagicPermanent creature : creatures) {
                game.doAction(ChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
            }
            game.doAction(new DestroyAction(creatures));
        }
    }
]
