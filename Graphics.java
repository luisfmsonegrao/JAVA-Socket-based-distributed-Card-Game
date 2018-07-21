
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
public class Graphics
{//classe que nao deve ser instanciada, serve para tratar da parte grafica
	public static void showHand(Jogador j1,int num_vaz,DataInputStream in, DataOutputStream out) throws IOException
	{//imprimir a mao
		ArrayList<Carta> mao = j1.getMao();
		int count = 1;
		out.writeUTF("cmv&"+num_vaz);
		for(Carta i:mao)
		{
			out.writeUTF("muc&" + i.getNaipe() + "&" + i.getFig() + "&" + count);
			count++;
		}
		
	}
	public static void showVaza(Mesa m,int num_vaza,DataInputStream in,DataOutputStream out) throws IOException
	{//mostrar a vaza
		ArrayList<Carta> vaza = m.getVaza();
		out.writeUTF("vzn&"+num_vaza);
		for (Carta i:vaza)
		{
			out.writeUTF("muv&" + i.getNaipe() + "&" + i.getFig() + "&" + i.getDono().getNome());
		}
	}
	public static void vazaResult(Jogador winner,Mesa m,DataOutputStream out) throws IOException
	{//mostrar o resultado da vaza
		out.writeUTF("vgp&" + winner.getNome() + "&" + m.getPontos() + "&" + winner.getParceiro().getNome());
	}
	public static void gameReport(Jogador team1, Jogador team2,DataOutputStream out) throws IOException
	{//mostrar o relatorio de fim de ronda
		if(team1.getWinner()){
			out.writeUTF("rdjg&" + team1.getPontos() + "&" + team2.getPontos());
		}
		else if(team2.getWinner())
		{
			out.writeUTF("rdjp&" + team1.getPontos() + "&" + team2.getPontos());
		}
		else
		{
			out.writeUTF("oue");
		}
		out.writeUTF("sg");
		out.writeUTF("sep2");
		for(int i=0;i<4;i++)
		{
			if (i<team1.getVitorias())
			{
				out.writeUTF("O");
			}
			else
			{
				out.writeUTF(" ");
			}
		}
		out.writeUTF("sep3");
		out.writeUTF("sep4");
		out.writeUTF("sep5");
		for(int i=0;i<4;i++)
		{
			if (i<team2.getVitorias())
			{
				out.writeUTF("O");
			}
			else
			{
				out.writeUTF(" ");
			}
		}
		out.writeUTF("sep1");
		if(team1.getWinner())
		{
			out.writeUTF("grp");
		}
		else if(team2.getWinner())
		{
			out.writeUTF("pms");
		}
		else
		{
			out.writeUTF("rte");
		}
	}
	public static void resultadoFinal(Jogador j1, DataInputStream in, DataOutputStream out) throws IOException
	{//mostrar o resultado final
		if (j1.getWinner())// podemos usar a variavel winner para testar se o jogador ganhou o jogo pois se assim for ele terá ganho a últimoa ronda, o que faz com que "winner" passe a true ate ao inicio ronda seguinte(que nao chega a existir)
		{
			out.writeUTF("gjp");
		}
		else
		{
			out.writeUTF("pjt");
		}
	}
}
