

public class Carta {//sobrecarrger método toString()
	
	private String naipe;
	private String figura;
	private Jogador dono;
	public Carta(String n, String v)
	{
		naipe = n;
		figura = v;
	}
	public int givePontos()
	{//devolver os pontos associados à carta na hashtable
		return (int)((double)Sueca.getHash().get(figura));
	}
	public String getNaipe()
	{
		return naipe;
	}
	public String getFig()
	{
		return figura;
	}
	public void setDono(Jogador j1)
	{
		dono=j1;	
	}
	public Jogador getDono()
	{
		return dono;
	}
	public String toString()
	{//método toString() sobrecarregado
		String s = ("|---|\n|"+this.getNaipe()+"  |\n|"+this.getFig()+" "+this.getFig() + "|\n|  "+this.getNaipe()+"|\n|---|");
		return s;
		
	}
}
