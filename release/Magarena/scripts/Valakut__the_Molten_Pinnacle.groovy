[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent other) {
            return (other.hasSubType(MagicSubType.Mountain) &&
                    other.isFriend(permanent) &&
                    MagicCondition.LEAST_FIVE_OTHER_MOUNTAINS.accept(other)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE_OR_PLAYER),
                    new MagicDamageTargetPicker(3),
                    other,
                    this,
                    "PN may\$ have SN deal 3 damage to target creature or player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (MagicCondition.LEAST_FIVE_OTHER_MOUNTAINS.accept(event.getRefPermanent()) == false || event.isNo()) {
                return;
            }
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
            });
        }
    }
]
