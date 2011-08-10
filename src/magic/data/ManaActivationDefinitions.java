package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManaActivationDefinitions {
	
	private final static void addManaActivation(final String name,final MagicManaActivation manaActivation) {
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(manaActivation);
	}
	
	private final static void addCreatureActivations(final String name,final List<MagicManaType> manaTypes) {
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(manaTypes,1));
		cardDefinition.setExcludeManaOrCombat();
	}
		
	private final static void addVividActivations(final String name,final MagicManaType manaType) {
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(MagicColor.NR_COLORS-1);
		for (final MagicColor color : MagicColor.values()) {
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType != manaType) {
				manaTypes.add(colorManaType);
			}
		}
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless,manaType),0));
		cardDefinition.addManaActivation(new MagicVividManaActivation(manaTypes));
	}
	
    public final static List<MagicManaType> getVividManaTypes(final MagicManaType manaType) {
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(MagicColor.NR_COLORS-1);
		for (final MagicColor color : MagicColor.values()) {
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType != manaType) {
				manaTypes.add(colorManaType);
			}
		}
        return manaTypes;
	}
}
