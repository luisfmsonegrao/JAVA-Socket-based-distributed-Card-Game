
import java.util.Scanner;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class Jogador {
	private String nome;
	private Jogador parceiro;
	private int pontos=0;
	private int vitorias=0;// numero de vazas ganhas num jogo
	private ArrayList<Carta> mao=new ArrayList<Carta>();
	private boolean winner=false;
	private int historico_vitorias = 0; //numero de jogos ganhos
	private String historico="Vitorias: 0 \n";
	public Jogador(String nome){
		this.nome=nome;
	}
	public void setHistoricoVitorias()
	{//atualizar o histórico de vitórias
		historico_vitorias++;
	}
	public int getHistoricoVitorias()
	{
		return historico_vitorias;
	}
	public void updateHistorico(String s)
	{//acrescentar informação ao histórico
		historico = historico+s;
	}
	public void refreshHistorico(String s)
	{//repor o hsitorico de um utilizador que ja tem conta, quando ha um login novo
		historico = s;
	}
	public void refreshHistoricoVitorias(int i)
	{//repor o numero de vitorias num login novo de um utilizador que ja tem conta
		historico_vitorias = i;
	}
	public void setParceiro(Jogador j1)
	{//definir o parceiro do jogador
		parceiro = j1;
	}
	public void setWinner(boolean b)
	{//controlo do estatudo do jogador no final de cada ronda (se ganhou ou nao a ronda)
		winner = b;
	}
	public void addVitorias(int i)
	{//incrementar numero de rondas ganhas
		vitorias+=i;
	}
	public boolean getWinner()
	{//verificar o estatuto do jogador no fim de cada ronda
		return winner;
	}
	public int getVitorias()
	{
		return vitorias;
	}
	public int getPontos()
	{
		return pontos;
	}
	public String getNome()
	{
		return nome;
	}
	public void addPontos(int i)
	{
		pontos += i;
	}
	public Jogador getParceiro()
	{
		return parceiro;
	}
	public ArrayList<Carta> getMao()
	{
		return mao;
	}
	public void shuffle(Baralho b)
	{//baralhar o baralho
		int tam = b.get_tamanho();
		int count=0;
		while(count<100){//100 é número arbitrário, o importante é ser grande
			int a=(int)(Math.random()*tam);
			int c=(int)(Math.random()*tam);
			Carta ctrl = b.getBar().get(a);//guarda a carta que esta em [a];
			b.getBar().set(a,b.getBar().get(c));//passa a que esta em [c] para [a]
			b.getBar().set(c,ctrl);//poe a que estava em [a] em [c]
			count++;
		}
	}
	public void cut(Baralho b)
	{//partir o baralho
		int tamanho = b.get_tamanho();
		int cut_ind = (int)(Math.random()*tamanho);
		ArrayList<Carta> metade = new ArrayList<Carta>();
		for (int i = 0; i<cut_ind;i++)
		{
			metade.add(b.getBar().get(i));
		}
		for (int i = cut_ind;i<tamanho;i++)
		{
			b.getBar().set(i-cut_ind,b.getBar().get(i));
		}
		for(int j = tamanho-cut_ind;j<tamanho;j++)
		{
			b.getBar().set(j,metade.get(j-tamanho+cut_ind));
		}
	}
	public void darCartas(Jogador j1, Jogador j2, Jogador j3,Baralho b)
	{//dar cartas a todos os jogadores
		for(int i=0;i<b.get_tamanho();i++)
		{
			if (i<10)
			{
				j1.getMao().add(b.get_card(i));
				j1.getMao().get(i).setDono(j1);
			}
			else if(i>=10 && i<20)
			{
				j2.getMao().add(b.get_card(i));
				j2.getMao().get(i-10).setDono(j2);
			}
			else if(i>=20 && i<30)
			{
				j3.getMao().add(b.get_card(i));
				j3.getMao().get(i-20).setDono(j3);
			}
			else
			{
				mao.add(b.get_card(i));
				mao.get(i-30).setDono(this);
			}
		}
	}
	public Carta jogarPrimeiro()
	{//método para o computador jogar em primeiro lugar
		Carta jogada = mao.get(0);
		for (Carta i: mao)
		{
			jogada=this.bestCard(jogada, i);
		}
		mao.remove(jogada);
		return jogada;
	}
	public Carta jogar(Mesa m,Baralho b)
	{//método para o computador jogar sem ser em primeiro lugar
		ArrayList<Carta> vaza = m.getVaza();
		String mainnaipe = vaza.get(0).getNaipe();
		Carta jogada=null;
		boolean temnaipe=false;
		boolean temtrunfo=false;
		for (Carta i: mao)
		{
			if(i.getNaipe().equals(mainnaipe))//se o naipe principal for trunfo e a carta for trunfo nao é executado o teste else if
			{
				temnaipe = true;
				jogada=i;
				break;
			}
			else if(i.getNaipe().equals(b.get_trunfo()))// se o naipe principal for trungo o else anterior diz logo se tem trungo ou nao. se nao for trunfo, se o jog tiver o naipe nao é preciso executar este if, se nao tiver executa.
			{
				temtrunfo=true;
				jogada=i;
			}
		}
		if(temnaipe)
		{
			for(Carta i: mao)
			{
				if(i.getNaipe().equals(jogada.getNaipe()))
				{
					jogada=this.bestCard(jogada, i);
				}
			}
		}
		else//só é executado se o jogador nao tiver o naipe principal, quer este naipe seja trunfo ou não
		{
			if(temtrunfo)
			{
				for (Carta i: mao)
				{
					if(i.getNaipe().equals(b.get_trunfo()))
					{
						jogada=this.bestCard(jogada, i);
					}
				}
			}
			else
			{
				jogada = mao.get(0);
				for (Carta i:mao)
				{
					jogada=this.bestCard(jogada,i);
				}
			}
			
		}
		mao.remove(jogada);
		return jogada;
	}
	public Carta userPlay(Mesa m,DataInputStream in,DataOutputStream out) throws IOException
	{//método para o utilizador jogar
		Carta jogada=null;
		ArrayList<Carta> vaza = m.getVaza();
		Scanner sc = new Scanner(System.in);
		boolean valid_in, valid_n, valid_f, tem_carta, tem_naipe, obedeceu;
		String np="",fig="";
		String[] naipes = {"E","ESPADAS","P","PAUS","C","COPAS","O","OUROS"};//inputs aceites para os naipes
		String[] figuras = {"2","3","4","5","6","d","dama","v","valete","r","rei","7","a","as"};//inputs aceites para a figura
		if (m.getVaza().isEmpty())
		{//se for o primeiro a jogar
			do{
				valid_in=false;
				valid_n=false;
				valid_f=false;
				tem_carta=false;
				out.writeUTF("iaf");
				fig=in.readUTF();
				if(!Server.check(fig)){
					break;
				}
				out.writeUTF("ion");
				np = in.readUTF();
				if(!Server.check(np)){
					break;
				}
				for(String i:naipes)
				{
					if(np.equalsIgnoreCase(i))
					{
						valid_n=true;
					}
				}
				for(String i:figuras)
				{
					if(fig.equalsIgnoreCase(i))
					{
						valid_f=true;
					}
				}
				if (valid_f && valid_n)
				{
					valid_in=true;
				}
				if (valid_in)
				{
					for (Carta c:mao)
					{
						if (Character.toString(np.charAt(0)).equalsIgnoreCase(c.getNaipe()) && Character.toString(fig.charAt(0)).equalsIgnoreCase(c.getFig()))
						{
							tem_carta=true;
							jogada = c;
						}
					}
					if (!tem_carta)
					{
						out.writeUTF("ntc");
					}
				}
				else
				{
					out.writeUTF("ii");
				}
			}while(!(valid_in && tem_carta));
			if(Server.check(np) && Server.check(fig)){
				out.writeUTF("cj");
			}
		}
		else
		{//se nao for o primeiro a jogar
			String mainnaipe = vaza.get(0).getNaipe();
			do{
				valid_in=false;
				valid_n=false;
				valid_f=false;
				tem_carta=false;
				tem_naipe=false;
				obedeceu=false;
				out.writeUTF("iaf");
				fig=in.readUTF();
				if(!Server.check(fig)){
					break;
				}
				out.writeUTF("ion");
				np = in.readUTF();
				if(!Server.check(np)){
					break;
				}
				for(String i:naipes)
				{
					if (i.equalsIgnoreCase(np))
					{
						valid_n=true;
					}
				}
				for(String i:figuras)
				{
					if(i.equalsIgnoreCase(fig))
					{
						valid_f=true;
					}
				}
				if(valid_n && valid_f)
				{
					valid_in=true;
				}
				if(valid_in)
				{
					for (Carta i:mao)
					{
						if(i.getNaipe().equals(mainnaipe))
						{
							tem_naipe=true;
						}
						if(i.getNaipe().equalsIgnoreCase(Character.toString(np.charAt(0))) && i.getFig().equalsIgnoreCase(Character.toString(fig.charAt(0))))
						{
							tem_carta=true;
							jogada=i;
						}
					}
					if(tem_carta)
					{
						if(tem_naipe)
						{
							if(jogada.getNaipe().equals(mainnaipe))
							{
								obedeceu=true;
							}
							else
							{
								out.writeUTF("non");
							}
						}
						else
						{
							obedeceu=true;//nao tem que obedecer a nenhuma regra
						}
					}
					else
					{
						out.writeUTF("ntc");
					}
				}
				else
				{
					out.writeUTF("idi");
				}
			}while(!(valid_in && tem_carta && obedeceu));
			if (Server.check(np) && Server.check(fig)){
				out.writeUTF("cj");
			}
		}
		if(jogada!=null){
			mao.remove(jogada);
		}
		return jogada;
	}
	public void clearStatus()
	{//reiniciar os pontos e a variavel winner no inicio de cada ronda nova
		pontos = 0;
		winner=false;
	}
	public Carta bestCard(Carta c1,Carta c2)
	{//metodo generico para comparar duas cartas
		if(Sueca.getHash().get(c1.getFig())>=Sueca.getHash().get(c2.getFig()))
		{
			return c1;
		}
		else
		{
			return c2;
		}
	}
	
	public String toString()
	{
		return historico;
	}
	
	public void reset(){
		vitorias = 0;
		winner=false;
		pontos = 0;	
	}
	
	public void abort(){
		updateHistorico("O jogador abandonou o jogo\n");
	}
	
	public void cleanHand(){
		mao.clear();
	}


	
}
