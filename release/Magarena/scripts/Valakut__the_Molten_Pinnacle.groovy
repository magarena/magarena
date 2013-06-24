[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent other) {
            return (other.hasSubType(MagicSubType.Mountain) &&
                    other.isFriend(permanent) &&
                    MagicCondition.LEAST_FIVE_OTHER_MOUNTAINS.accept(other)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER),
                    new MagicDamageTargetPicker(3),
                    other,
                    this,
                    "PN may\$ have SN deal 3 damage to target creature or player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (MagicCondition.LEAST_FIVE_OTHER_MOUNTAINS.accept(event.getRefPermanent()) == false) {
                return;
            }
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        target,
                        3
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
