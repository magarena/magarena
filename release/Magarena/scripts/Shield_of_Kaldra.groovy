[    
	new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_EQUIPMENT
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Indestructible);
        }
		@Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return (target.getName() == "Sword of Kaldra" || target.getName() == "Shield of Kaldra" || target.getName() == "Helm of Kaldra");
        }
    },
	new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
					otherPermanent.getName() == "Kaldra" &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAttachAction(
                event.getPermanent(),
                event.getRefPermanent()
            ));
        }
    }
]