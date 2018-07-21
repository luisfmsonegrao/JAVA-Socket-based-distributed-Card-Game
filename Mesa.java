
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class Mesa
{
	private int pontos=0;
	private ArrayList<Carta> vaza = new ArrayList<Carta>();
	public int getPontos()
	{//devolver os pontos que "estão em cima da mesa"
		return pontos;
	}
	public Jogador whoWins(Jogador primeiro, Jogador segundo, Jogador terceiro, Jogador quarto, Jogador user, Baralho b,int num_vaza,DataInputStream in, DataOutputStream out) throws IOException
	{//método onde se decide quem ganha e se atribuem os pontos ao vencedor e seu parceiro
		String trunfo=b.get_trunfo();
		Graphics.showHand(user,num_vaza,in,out);
		out.writeUTF("trfo&" + trunfo);
		if(primeiro.equals(user))
		{
			vaza.add(primeiro.userPlay(this,in,out));
			if(vaza.get(0)!=null){
				vaza.add(segundo.jogar(this, b));
				vaza.add(terceiro.jogar(this, b));
				vaza.add(quarto.jogar(this, b));
			}
		}
		else if(segundo.equals(user))
		{
			vaza.add(primeiro.jogarPrimeiro());
			Graphics.showVaza(this,num_vaza,in,out);
			vaza.add(segundo.userPlay(this,in,out));
			if(vaza.get(1)!=null){
				vaza.add(terceiro.jogar(this, b));
				vaza.add(quarto.jogar(this, b));
			}
		}
		else if(terceiro.equals(user))
		{
			vaza.add(primeiro.jogarPrimeiro());
			vaza.add(segundo.jogar(this,b));
			Graphics.showVaza(this,num_vaza,in,out);
			vaza.add(terceiro.userPlay(this,in,out));
			if(vaza.get(2)!=null){
				vaza.add(quarto.jogar(this, b));
			}
		}
		else if(quarto.equals(user))
		{
			vaza.add(primeiro.jogarPrimeiro());
			vaza.add(segundo.jogar(this,b));
			vaza.add(terceiro.jogar(this, b));
			Graphics.showVaza(this,num_vaza,in,out);
			vaza.add(quarto.userPlay(this,in,out));
		}//ecapsular mais os métodos
		if(!vaza.contains(null)){
			pontos = atribuirPontos(vaza);
			String mainnaipe = vaza.get(0).getNaipe();
			Carta winner = vaza.get(0);
			for(Carta c:vaza)
			{
				if(c.getNaipe().equals(mainnaipe))//se o main inicial for trunfo, o else-if nunca vai correr. se o main inicial nao for trunfo mas houver um trunfo jogado, o else-if corre uma vez e o naipe principal passa a trunfo
				{
					if(Sueca.getHash().get(c.getFig())>Sueca.getHash().get(winner.getFig()))
					{
						winner=c;
					}
				}
				else if (c.getNaipe().equals(trunfo))
				{
					mainnaipe = trunfo;
					winner = c;
				}	
			}
			winner.getDono().addPontos(pontos);
			winner.getDono().getParceiro().addPontos(pontos);
			Graphics.showVaza(this,num_vaza,in,out);
			Graphics.vazaResult(winner.getDono(),this,out);
			return winner.getDono();
		}
		else{
			return null;
		}
	}
	public ArrayList<Carta> getVaza()
	{
		return vaza;
	}
	public void clearVaza()
	{//reiniciar a vaza antes da vaza seguinte
		vaza.clear();
		pontos=0;
	}
	public int atribuirPontos(ArrayList<Carta> v)
	{//calcular os pontos a atribuir aos vencedores de cada vaza
		for(Carta i:vaza)
		{
			pontos+=i.givePontos();
		}
		return pontos;
	}
}
