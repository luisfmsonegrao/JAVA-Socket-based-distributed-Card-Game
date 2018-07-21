
import java.util.ArrayList;
public class Baralho {
	private int tamanho=40;
	private ArrayList<Carta> baralho1=new ArrayList<Carta>();
	private String trunfo;
	public Baralho()
	{
		String[] naipe={"E","P","C","O"};
		String[] fig = {"2","3","4","5","6","D","V","R","7","A"};
		for (int i=0;i<4;i++)
		{
			for(int j=0;j<10;j++)
			{
				baralho1.add(new Carta(naipe[i],fig[j]));
			}
		}
	}
	public Carta get_card(int i){
		return baralho1.get(i);
	}
	public void setTrunfo()
	{
		trunfo=baralho1.get(0).getNaipe();
	}
	public String get_trunfo(){
		return trunfo;	
	}
	public int get_tamanho(){
		return tamanho;
	}
	public void clear(){
		baralho1.clear();
	}
	public ArrayList<Carta> getBar()
	{
		return baralho1;
	}
		
}
