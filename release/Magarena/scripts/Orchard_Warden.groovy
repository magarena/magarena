[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent != permanent &&
                   otherPermanent.isCreature() &&
                   otherPermanent.hasSubType(MagicSubType.Treefolk) &&
                   otherPermanent.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("gain life?"),
                    otherPermanent,
                    this,
                    "PN may\$ gain life equal to RN's toughness."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),event.getRefPermanent().getToughness()));
            }
        }
    }
]
