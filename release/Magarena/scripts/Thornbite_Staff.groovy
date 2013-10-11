def Ping = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Damage"
) {

     @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
			new MagicPayManaCostEvent(source,"{2}"),
            new MagicTapEvent(source)
        ];
    }

    @Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
		return new MagicEvent(
			source,
			MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
			new MagicDamageTargetPicker(1),
			this,
			"SN deals 1 damage to target creature or player\$."
		);
	}

   @Override
	public void executeEvent(final MagicGame game, final MagicEvent event) {
		final MagicSource source=event.getSource();
		event.processTarget(game,new MagicTargetAction() {
			public void doAction(final MagicTarget target) {
				final MagicDamage damage1=new MagicDamage(source,target,1);
				game.doAction(new MagicDealDamageAction(damage1));
			}
		});
	}
};
[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(Ping);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) { 
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
	new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (equippedCreature.isValid() && otherPermanent.isCreature()) ?
                new MagicEvent(
                    equippedCreature,
                    this,
                    "Untap SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }
    },
	new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
					otherPermanent.hasSubType(MagicSubType.Shaman)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
					otherPermanent,
                    this,
                    "You may\$ attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
				game.doAction(new MagicAttachAction(
					event.getPermanent(),
					event.getRefPermanent()
				));
			}
        }
    }
]
