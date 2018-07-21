
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
public class Server
{
	public static void main(String args[]) throws IOException
	{
		
		String pass,user;
		Hashtable<String,String> user_pass = new Hashtable<String,String>();//guardar nome de utilizador e pass
		Hashtable<String,String> user_history = new Hashtable<String,String>();//guardar o texto com o historico do jogador
		Hashtable<String,Integer> user_victories = new Hashtable<String,Integer>();//guardar as vitorias de cada jogador para as repor num novo login
		String answer,querjogar;
		Jogador j1=null;
		ServerSocket servidor = new ServerSocket(7000);
		boolean datavalid,temconta,user_pass_valid,goon;
		while (true){
			datavalid = false;// input valido na questão"ja tem conta?"
			temconta=false;
			user_pass_valid=false;//validar nome de utilizador e password
			goon=true;// jogador quer continuar a jogar
			Socket s1 = servidor.accept();
			OutputStream out = s1.getOutputStream();
			DataOutputStream dataOut = new DataOutputStream(out);
			InputStream in = s1.getInputStream();
			DataInputStream dataIn = new DataInputStream(in);
			dataOut.writeUTF("jtc");
			while(!datavalid){
				datavalid=false;//desnecessario
				temconta=false;//desnecessario
				answer=dataIn.readUTF();
				if (answer.equalsIgnoreCase("Sim")){
					dataOut.writeUTF("lg");
					datavalid=true;
					temconta=true;
				}
				else if(answer.equalsIgnoreCase("Nao") || answer.equals("Não") || answer.equals("não")){
					datavalid=true;
					dataOut.writeUTF("cc");
				}
				else{
					dataOut.writeUTF("di");
				}
			}
			while(!user_pass_valid){
				if(temconta){
					dataOut.writeUTF("inu");
					user=dataIn.readUTF();
					dataOut.writeUTF("ip");
					pass=dataIn.readUTF();
					if(user_pass.containsKey(user)){
						if (pass.equals(user_pass.get(user))){
							user_pass_valid=true;
							dataOut.writeUTF("lbs");
							j1 = new Jogador(user);
							j1.refreshHistorico(user_history.get(user));
							j1.refreshHistoricoVitorias(user_victories.get(user));
						}
						else{
							dataOut.writeUTF("pit");
						}
					}
					else{
						dataOut.writeUTF("ui");
						dataOut.flush();
					}
				}
				else{
					dataOut.writeUTF("inu");
					user=dataIn.readUTF();
					dataOut.writeUTF("ip");
					j1 = new Jogador(user);
					pass=dataIn.readUTF();
					if(!user_pass.containsKey(user)){
						if(!user.isEmpty() && !pass.isEmpty()){
							user_pass_valid=true;
							user_pass.put(user, pass);
							dataOut.writeUTF("cri");
						}
						else{
							dataOut.writeUTF("piui");
						}
					}
					else{
						dataOut.writeUTF("uju");
					}
				}
			}
			dataOut.writeUTF(j1.toString()+"\n");
			while(goon){
				Sueca jogo = new Sueca(j1);
				goon=jogo.start(dataIn,dataOut);//o cliente pode querer sair a meio do jogo. se, e só se, quiser, .start() devolve false
				if(!goon){
					if(user_history.containsKey(j1.getNome())){
						user_history.replace(j1.getNome(),user_history.get(j1.getNome()), j1.toString());
					}
					else{
						user_history.put(j1.getNome(), j1.toString());
					}
					if(user_victories.containsKey(j1.getNome())){
						user_victories.replace(j1.getNome(),user_victories.get(j1.getNome()), j1.getHistoricoVitorias());
					}
					else{
						user_victories.put(j1.getNome(), j1.getHistoricoVitorias());
					}
					j1.reset();
					break;
				}
				querjogar=dataIn.readUTF();
				if (querjogar.equalsIgnoreCase("n") || querjogar.equalsIgnoreCase("nao") || querjogar.equalsIgnoreCase("não"))
				{//no final de cada jogo, averiguar se o utilizdor quer continuar
					goon=false;
					if(user_victories.containsKey(j1.getNome())){
						user_victories.replace(j1.getNome(),user_victories.get(j1.getNome()), j1.getHistoricoVitorias());
					}
					else{
						user_victories.put(j1.getNome(), j1.getHistoricoVitorias());
					}
				}
				j1.reset();//repõe as vitorias (em vazas) e os pontos a zero
				if(user_history.containsKey(j1.getNome())){
					user_history.remove(j1.getNome());
				}
				user_history.put(j1.getNome(),j1.toString());
			}
		j1.cleanHand();
		dataOut.close();
		dataIn.close();
		s1.close();
		}
	}
	
	public static boolean check(String s){//verificar se o cliente enviou "exit"
		if(s.equalsIgnoreCase("exit")){
			return false;
		}
		else{
			return true;
		}
	}
	
}
