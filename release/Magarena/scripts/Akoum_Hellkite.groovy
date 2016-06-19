[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int amount = otherPermanent.hasSubType(MagicSubType.Mountain) ? 2 : 1;
            return (otherPermanent.isLand() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    amount,
                    this,
                    "SN deals RN damage to target creature or player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
            });
        }
    }
]
