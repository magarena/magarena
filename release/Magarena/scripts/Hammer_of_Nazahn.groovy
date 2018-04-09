[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isEquipment() && otherPermanent.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                new MagicMayChoice(TARGET_CREATURE_YOU_CONTROL),
                    otherPermanent,
                    this,
                    "PN may\$ attach RN to target creature\$ PN controls"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new AttachAction(event.getRefPermanent(), it));
                });
            }
        }
    }
]
