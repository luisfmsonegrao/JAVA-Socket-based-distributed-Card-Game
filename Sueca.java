
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Hashtable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class Sueca
{//classe onde se controla o jogo
	private static Hashtable<String, Double> hash = new Hashtable<String, Double>();
	private Jogador j1;
	private Jogador j2 = new Jogador("PC1");
	private Jogador j3 = new Jogador("Parceiro");
	private Jogador j4 = new Jogador("PC2");
	private Jogador[] list = {j1, j2, j3, j4};
	
	public Jogador getJogador(String nome){
		for (Jogador j:list){
			if (j.getNome().equals(nome)){
				return j;
			}
		}
		return null;
	}
	
	public Sueca(Jogador j)
	{
		hash.put("2",(0.1));//como sao valores menores que 1, o casting para int vai arredondar os pontos destas cartas para zero
		hash.put("3", (0.2));//assim podemos comparar os valores double presentes na hashtable para decidir que carta jogar (qual a maior)
		hash.put("4", (0.3));//e fazer a atribuição de pontos fazendo o casting para (int) dos valores da hashtable
		hash.put("5", (0.4));
		hash.put("6", (0.45));
		hash.put("D", 2.0);
		hash.put("V", 3.0);
		hash.put("R", 4.0);
		hash.put("7", 10.0);
		hash.put("A", 11.0);
		j1=j;
	}
	public static Hashtable<String,Double> getHash()
	{//devolve a hashtable que associa a cada figura os respetivos valores
		return hash;
	}
	public boolean start(DataInputStream in, DataOutputStream out) throws IOException
	{//controlo do fluxo de jogo
		String answer="";
		boolean querjogar=true;
		Mesa m = new Mesa();
		j1.setParceiro(j3);
		j3.setParceiro(j1);
		j2.setParceiro(j4);
		j4.setParceiro(j2);
		int whoshuffles = 0,ronda_num=0;
		Jogador primeiro=j1,segundo=j2,terceiro=j3,quarto=j4;
		while(j1.getVitorias()<4 && j2.getVitorias()<4 && querjogar){//ciclo de controlo de cada ronda
			j1.clearStatus();
			j2.clearStatus();
			j3.clearStatus();
			j4.clearStatus();
			answer="";
			while(!(answer.equals("\n"))){
				out.writeUTF("per");
				answer=in.readUTF();
				if(!Server.check(answer)){
					j1.abort();
					querjogar=false;
					break;
				}
			}
			if(!querjogar){
				break;
			}
			int vaza_num=1;
			Baralho b1 = new Baralho();
			switch(whoshuffles){//atribuir os deveres de baralhar, partir, dar cartas e começar a jogar
				case 0:
					primeiro = j1;
					segundo = j2;
					terceiro = j3;
					quarto = j4;
					j1.shuffle(b1);
					j3.cut(b1);
					j4.darCartas(j1,j2,j3,b1);
					whoshuffles++;
					break;
				case 1:
					primeiro = j2;
					segundo = j3;
					terceiro = j4;
					quarto = j1;
					j2.shuffle(b1);
					j4.cut(b1);
					j1.darCartas(j2,j3,j4,b1);
					whoshuffles++;
					break;
				case 2:
					primeiro = j3;
					segundo = j4;
					terceiro = j1;
					quarto = j2;
					j3.shuffle(b1);
					j1.cut(b1);
					j2.darCartas(j1,j3,j4,b1);
					whoshuffles++;
					break;
				case 3:
					primeiro = j4;
					segundo = j1;
					terceiro = j2;
					quarto = j3;
					j4.shuffle(b1);
					j2.cut(b1);
					j3.darCartas(j1, j2, j4, b1);
					whoshuffles=0;
					break;
			}
			b1.setTrunfo();//podemos definir o trunfo aqui porque não estamos a eliminar as cartas do baralho
			while(vaza_num<=10)
			{//ciclo de controlo de cada vaza
				Jogador vencedor = m.whoWins(primeiro,segundo,terceiro,quarto,j1,b1,vaza_num,in,out);
				if (vencedor==null){//o vencedor só é null quando o utilizador insere "exit"
					j1.abort();
					querjogar=false;
					break;
				}
				primeiro = vencedor;
				if(vencedor.equals(j1))
				{
					segundo = j2;
					terceiro = j3;
					quarto = j4;
				}
				else if (vencedor.equals(j2))
				{
					segundo = j3;
					terceiro = j4;
					quarto = j1;
				}
				else if (vencedor.equals(j3))
				{
					segundo = j4;
					terceiro = j1;
					quarto = j2;
				}
				else
				{
					segundo = j1;
					terceiro = j2;
					quarto = j3;
				}
				m.clearVaza();
				vaza_num++;
			}
			if(querjogar){
				out.writeUTF("tvr");
				j1.updateHistorico("Ronda numero " + ronda_num + ": " + j1.getPontos() + "pontos.\n");
				if (j1.getPontos()>60 && j1.getPontos()<90)
				{
					j1.setWinner(true);
					j1.getParceiro().setWinner(true);
					j2.setWinner(false);
					j2.getParceiro().setWinner(false);
					j1.addVitorias(1);
					j1.getParceiro().addVitorias(1);
				}
				else if (j1.getPontos()>=90 && j1.getPontos()<120)
				{
					j1.setWinner(true);
					j1.getParceiro().setWinner(true);
					j2.setWinner(false);
					j2.getParceiro().setWinner(false);
					j1.addVitorias(2);
					j1.getParceiro().addVitorias(2);
				}
				else if (j1.getPontos()==120)
				{
					j1.setWinner(true);
					j1.getParceiro().setWinner(true);
					j2.setWinner(false);
					j2.getParceiro().setWinner(false);
					j1.addVitorias(4);
					j1.getParceiro().addVitorias(4);
				}
				else if (j1.getPontos()>=30 && j1.getPontos()<60)
				{
					j2.setWinner(true);
					j2.getParceiro().setWinner(true);
					j1.setWinner(false);
					j1.getParceiro().setWinner(false);
					j2.addVitorias(1);
					j2.getParceiro().addVitorias(1);
				}
				else if (j1.getPontos()>0 && j1.getPontos()<30)
				{
					j2.setWinner(true);
					j2.getParceiro().setWinner(true);
					j1.setWinner(false);
					j1.getParceiro().setWinner(false);
					j2.addVitorias(2);
					j2.getParceiro().addVitorias(2);
				}
				else if (j1.getPontos() == 0)
				{
					j2.setWinner(true);
					j2.getParceiro().setWinner(true);
					j1.setWinner(false);
					j1.getParceiro().setWinner(false);
					j2.addVitorias(4);
					j2.getParceiro().addVitorias(4);
				}
				else if (j1.getPontos()==60){
					j1.setWinner(false);
					j2.setWinner(false);
					j3.setWinner(false);
					j4.setWinner(false);
				}
				Graphics.gameReport(j1,j2,out);
				out.writeUTF("" + j1.getVitorias());
				out.writeUTF("" + j2.getVitorias());
				ronda_num++;
			}
		}
		if(querjogar){
			if (j1.getVitorias()>=4){
				j1.setHistoricoVitorias();
				j1.updateHistorico("Vitorias: "+ j1.getHistoricoVitorias() + "\n");
			}
			Graphics.resultadoFinal(j1,in,out);
			out.writeUTF("toj");
		}
		return querjogar;
	}
}
