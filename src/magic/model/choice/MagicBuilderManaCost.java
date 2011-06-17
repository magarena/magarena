package magic.model.choice;

import java.util.Arrays;
import java.util.List;

import magic.model.MagicCostManaType;

public class MagicBuilderManaCost {

	public static final MagicBuilderManaCost ZERO_COST=new MagicBuilderManaCost();
	
	private final int amounts[];
	private MagicCostManaType compressedTypes[];
	private int compressedAmounts[];
	private int typeCount;
	private int minimumAmount;
	private boolean hasX;

	public MagicBuilderManaCost() {
		amounts=new int[MagicCostManaType.NR_OF_TYPES];
		compressedTypes=new MagicCostManaType[0];
		compressedAmounts=new int[0];
		typeCount=0;
		minimumAmount=0;
		hasX=false;
	}
	
	public MagicBuilderManaCost(final MagicBuilderManaCost cost) {
		amounts=Arrays.copyOf(cost.amounts,cost.amounts.length);;
		compressedTypes=Arrays.copyOf(cost.compressedTypes,cost.compressedTypes.length);
		compressedAmounts=Arrays.copyOf(cost.compressedAmounts,cost.compressedAmounts.length);
		typeCount=cost.typeCount;
		minimumAmount=cost.minimumAmount;
		hasX=cost.hasX;
	}

	public void compress() {
		compressedTypes=new MagicCostManaType[typeCount];
		compressedAmounts=new int[typeCount];
		int compressedIndex=0;
		for (int index=0;index<MagicCostManaType.NR_OF_TYPES;index++) {

			final int amount=amounts[index];
			if (amount>0) {
				compressedTypes[compressedIndex]=MagicCostManaType.values()[index];
				compressedAmounts[compressedIndex]=amount;
				compressedIndex++;
			}
		}
	}
	
	public MagicCostManaType[] getTypes() {
		return compressedTypes;
	}
	
	public int[] getAmounts() {
		return compressedAmounts;
	}
	
	public int getMinimumAmount() {
		return minimumAmount;
	}
	
	public void setHasX() {
		addType(MagicCostManaType.Colorless,1);
		hasX=true;
	}
		
	public boolean hasX() {
		return hasX;
	}
	
	public int getX(final int amount) {
		return hasX?amount-minimumAmount+1:0;
	}

	public boolean isEmpty() {
		return typeCount==0;
	}
	
	public void addType(final MagicCostManaType type,final int amount) {
		if (amount>0) {
			final int index=type.ordinal();
			if (amounts[index]==0) {
				typeCount++;
			}
			amounts[index]+=amount;
			minimumAmount+=amount;
		}
	}
	
	public void removeType(final MagicCostManaType type,final int amount) {
		final int index=type.ordinal();
		amounts[index]-=amount;
		if (amounts[index]<=0) {
			typeCount--;
		}
		minimumAmount-=amount;
	}
	
	public void addTypes(final List<MagicCostManaType> types) {
		for (final MagicCostManaType type : types) {
			addType(type,1);
		}
		compress();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder=new StringBuilder();
		for (int index=0;index<compressedTypes.length;index++) {
			
			builder.append(compressedTypes[index]).append('=').append(compressedAmounts[index]).append(' ');
		}
		builder.append("Total=").append(minimumAmount);
		return builder.toString();
	}
}
