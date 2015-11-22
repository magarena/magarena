[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount=permanent.getPower();
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(),
                this,
                "PN may\$ gain life equal to SN's power ("+amount+")."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),event.getPermanent().getPower()));
            }
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int amount=otherPermanent.getPower();
            return (otherPermanent!=permanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.hasAbility(MagicAbility.Flying) &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    otherPermanent,
                    this,
                    "PN may\$ gain life equal to RN's power ("+amount+")."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                final MagicPermanent permanent=event.getRefPermanent();
                game.doAction(new ChangeLifeAction(event.getPlayer(),permanent.getPower()));
            }
        }
    }
]
