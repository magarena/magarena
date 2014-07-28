[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getNrOfPermanents(MagicSubType.Dragon);
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target creature or player\$ equal to the number of Dragons PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Dragon);
                final MagicDamage damage = new MagicDamage(event.getSource(),it,amount);
                game.doAction(new MagicDealDamageAction(damage));
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
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    otherPermanent,
                    this,
                    "RN deals damage to target creature or player\$ equal to the number of Dragons PN controls."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            event.processTarget(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Dragon);
                final MagicDamage damage = new MagicDamage(permanent,it,amount);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
