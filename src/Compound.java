class Compound
{
	private String compound;
	private int BondCount;
	
	Compound(String compound , int Bondc)
	{
		this.compound = compound;
		BondCount = Bondc;
	}
	
	public int BondValue()
	{
		return BondCount;
	}
	
	public String CompoundValue()
	{
		return compound;
	}
	
	public void setCompoundValue(String toSet)
	{
		compound = toSet;
	}
}