[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getNrOfPermanents(MagicSubType.Dragon);
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target creature or player\$ equal to the number of Dragons PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Dragon);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int amount = permanent.getController().getNrOfPermanents(MagicSubType.Dragon);
            return (otherPermanent.hasSubType(MagicSubType.Dragon) && permanent != otherPermanent && otherPermanent.isFriend(permanent)) ? 
                new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    otherPermanent,
                    this,
                    "RN deals damage to target creature or player\$ equal to the number of Dragons PN controls."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Dragon);
                game.doAction(new DealDamageAction(event.getRefPermanent(),it,amount));
            });
        }
    }
]
