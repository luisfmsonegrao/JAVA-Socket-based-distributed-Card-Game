import java.io.DataInputStream;
import java.util.Hashtable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
public class newClient
{
	private static Hashtable<String,String> protocolo;
	public static void setHash(){
		protocolo = new Hashtable<String,String>();
		protocolo.put("jtc","Já tem conta?(Sim/Não)\n");//Ja Tem Conta
		protocolo.put("lg","LOGIN\n");//LoGin
		protocolo.put("cc","Criar Conta\n");//Criar Conta
		protocolo.put("di","Dados Inválidos\n");//Dados Invalidos
		protocolo.put("inu","Insira Nome de Utilizador\n");//Insira Nome de Utilizador
		protocolo.put("ip","Insira Password\n");//Insira Password
		protocolo.put("lbs","Login Bem Sucedido\n");//Login Bem Sucedido
		protocolo.put("pit", "Password incorreta. Tente outra vez\n");//Password Incorreta Tente outra vez
		protocolo.put("ui","Username Inválido\n");//Username Inválido
		protocolo.put("uju", "Username já usado\n");
		protocolo.put("piui", "A password e o username não podem ser vazios\n");
		protocolo.put("cri", "Conta Criada\n");//conta CRIada
		protocolo.put("per","Prima enter para começar a nova ronda\n");//Prima Enter para começar nova Ronda
		protocolo.put("trfo",">>>>>>>>>>>>>>>> O Trunfo é %s <<<<<<<<<<<<<<<<");//trunfo
		protocolo.put("tvr", "Terminou a última vaza da ronda\n");// Terminou ... Vaza da Ronda
		protocolo.put("toj","Terminou o jogo\n");//Terminou O Jogo
		protocolo.put("iaf","Insira a figura\n");//Insira A Figura
		protocolo.put("ion", "Insira o naipe\n");//Insira O Naipe
		protocolo.put("cj", "Carta jogada\n");//Carta Jogada
		protocolo.put("ntc","Não tem essa carta\n");//Nao Tem essa Carta
		protocolo.put("ii", "Input inválido\n");//Input Invalido
		protocolo.put("non", "Não obedeceu ao naipe\n");//Nao Obedeceu ao Naipe
		protocolo.put("idi", "Introduziu dados inválidos\n");//Introduziu Dados Inválidos
		protocolo.put("grp", "Ganhou a ronda, parabéns!\n");//Ganhou a Ronda, Parabéns
		protocolo.put("pms", "Perdeu, melhor sorte para a próxima\n");//Perdeu, Melhor Sorte....
		protocolo.put("rte", "A ronda terminou empatada\n");//a Ronda Terminou Empatada
		protocolo.put("gjp", "Ganhou o jogo! Parabéns!\n");//Ganhou o Jogo Parabéns
		protocolo.put("pjt","Perdeu o jogo! Tente outra vez!\n");//Perdeu o Jogo, Tente...
		protocolo.put("oue","Ocorreu um empate\n");//Ocorreu Um Empate
		protocolo.put("sg", "========================SITUAÇÃO GERAL========================\n");//Sit... Geral
		protocolo.put("sep1","\n==============================================================\n");//formatacao de info
		protocolo.put("sep2", "Par 1: ");//formatacao de info
		protocolo.put("sep3", " Vitórias\n");//formatacao de info
		protocolo.put("sep4", "       ||||\n       ----\n       ||||\n");//formatacao de info
		protocolo.put("sep5", "Par 2: ");//formatacao de info
		protocolo.put("cmv", "========================= Cartas na sua mão na vaza %,d =========================\n");//Carta na sua Mão na Vaza...
		protocolo.put("vzn", "========================= Vaza número %s =========================\n");//VaZa Numero
		protocolo.put("muc","|---|\n|%s  |\n|%s %s|\n|  %s|\n|---| Carta--> %s");//Mostrar Uma Carta
		protocolo.put("muv","|---|\n|%s  |\n|%s %s|\n|  %s|\n|---| Carta jogada por %s");//Mostrar Uma Vaza
		protocolo.put("vgp", "==================================================\n+++++ A vaza foi ganha por %s +++++\n|%s pontos a atribuir a %s e %s|\n==================================================\n");//Vaza Ganha Por...
		protocolo.put("rdjg", "=========================Relatório de Jogo======================\nVocê e o seu parceiro ganharam e conseguiram %s pontos.\nOs seus rivais perderam, só conseguiram %s pontos.\n");//Relatório Do Jogo (se Ganhou)...
		protocolo.put("rdjp", "=========================Relatório de Jogo======================\nVocê e o seu parceiro perderam com %s pontos.\nOs seus rivais ganharam com %s pontos\n");//Relatório De Jogo (se Perdeu)
	}
		
		
		
	// 'throws IOException' enables us to write the code without try/catch blocks
	// but it also keeps us from handling possible IO errors 
	// (when for instance there is a problem when connecting with the other party) 
	public static void main(String[] args) throws IOException 
	{
		setHash();
		boolean goon=true;
		int v_team,v_rivais;
		String querjogar,mts;//mts message to send
		String[] packs=new String[0];
		String answer="";
		Scanner sc = new Scanner(System.in);
		Socket socket = new Socket("localhost", 7000);
		InputStream in = socket.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		OutputStream out = socket.getOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		print(dataIn.readUTF(),packs);
		dataOut.writeUTF(sc.nextLine());
		dataOut.flush();
		packs=readMessage(dataIn.readUTF());
		answer=packs[0];
		print(answer,packs);
		while(answer.equals("di")){
			dataOut.writeUTF(sc.nextLine());
			packs=readMessage(dataIn.readUTF());
			answer=packs[0];
			print(answer,packs);
		}
		print(dataIn.readUTF(),packs);;//username
		dataOut.writeUTF(sc.nextLine());
		print(dataIn.readUTF(),packs);//password
		dataOut.writeUTF(sc.nextLine());
		packs=readMessage(dataIn.readUTF());
		answer=packs[0];
		print(answer,packs);
		if(answer.equals("cri") || answer.equals("lbs")){}
		else if(answer.equals("pit") || answer.equals("ui") || answer.equals("uju") || answer.equals("piui")){
			while(answer.equals("pit") || answer.equals("ui") || answer.equals("uju") || answer.equals("piui")){
				packs=readMessage(dataIn.readUTF());
				answer=packs[0];
				print(answer,packs);
				dataOut.writeUTF(sc.nextLine());
				packs=readMessage(dataIn.readUTF());
				answer=packs[0];
				print(answer,packs);
				dataOut.writeUTF(sc.nextLine());
				packs=readMessage(dataIn.readUTF());
				answer=packs[0];
				print(answer,packs);//fim do login
			}
		}
		packs=readMessage(dataIn.readUTF());
		answer=packs[0];
		print(answer,packs);
		int num_ronda=1;
		while (goon){
			while(!answer.equalsIgnoreCase("toj") && goon){
				packs=readMessage(dataIn.readUTF());
				answer=packs[0];
				print(answer,packs);
				while(answer.equals("per")){
					mts=sc.nextLine();
					if(!check(mts)){
						dataOut.writeUTF(mts); //enviar exit sem "\n"
						goon=false;
						break;
					}
					else{
						dataOut.writeUTF(mts+"\n"); //só a premir enter mts fica vazio, tem que se acrescentar um "\n"
					}
					packs=readMessage(dataIn.readUTF());
					answer=packs[0];
					print(answer,packs);
				}
				int numero_vaza=1;
				while(!answer.equals("tvr") && goon){
					while(!(answer.equals("iaf"))){
						packs=readMessage(dataIn.readUTF());
						answer=packs[0];
						print(answer,packs);
					}
					int it=1;
					while(!(answer.equals("cj"))){
						if (it>1){
							print(dataIn.readUTF(),packs);
						}
						mts = sc.nextLine();
						dataOut.writeUTF(mts);
						if(!check(mts)){
							goon=false;
							break;
						}
						print(dataIn.readUTF(),packs);
						mts=sc.nextLine();
						dataOut.writeUTF(mts);
						if(!check(mts)){
							goon=false;
							break;
						}
						packs=readMessage(dataIn.readUTF());
						answer=packs[0];
						print(answer,packs);
						it++;
					}
					while(!answer.equals("vgp") && goon){
						packs=readMessage(dataIn.readUTF());
						answer=packs[0];
						print(answer,packs);
					}
					if(goon){
						packs=readMessage(dataIn.readUTF());
						answer=packs[0];
						print(answer,packs);
						numero_vaza++;
					}
				}
				while(!(answer.equals("grp")||answer.equals("pms")||answer.equals("rte")) && goon){
					packs=readMessage(dataIn.readUTF());
					answer=packs[0];
					print(answer,packs);
				}
				if(goon){
					v_team=Integer.parseInt(dataIn.readUTF());//numero de vitorias (rondas) do jogador
					v_rivais=Integer.parseInt(dataIn.readUTF());
					num_ronda++;
					if(v_team>=4 || v_rivais >=4){
						while(!answer.equals("toj")){
							packs=readMessage(dataIn.readUTF());
							answer=packs[0];
							print(answer,packs);
						}
					}
				}
			}
			if(goon){
				answer="";
				System.out.println("Quer jogar outra vez? (S/N)");
				querjogar = sc.nextLine();
				if (querjogar.equalsIgnoreCase("n") || querjogar.equalsIgnoreCase("não") || querjogar.equalsIgnoreCase("nao")){
					goon=false;
				}
				dataOut.writeUTF(querjogar);
			}
		}
		sc.close();
		dataOut.close();
		dataIn.close();
		socket.close();
		print("Shutting down",packs);
	}
	
	public static void print(String s,String[] p){
		if(protocolo.containsKey(s)){
			if (s.equals("cmv")){
				System.out.print(String.format(protocolo.get(s),Integer.parseInt(p[1])));//numero vaza
			}
			else if(s.equals("muc")){
				System.out.println(String.format(protocolo.get(s),p[1],p[2],p[2],p[1],p[3]));//naipe,fig,fig,naipe,num_carta
			}
			else if (s.equals("vzn")){
				System.out.print(String.format(protocolo.get(s),p[1])); // numero vaza
			}
			else if (s.equals("muv")){
				System.out.println(String.format(protocolo.get(s), p[1],p[2],p[2],p[1],p[3]));
			}
			else if (s.equals("vgp")){
				System.out.println(String.format(protocolo.get(s), p[1],p[2],p[1],p[3]));
			}
			else if (s.equals("rdjg")){
				System.out.println(String.format(protocolo.get(s), p[1],p[2]));
			}
			else if(s.equals("rdjp")){
				System.out.println(String.format(protocolo.get(s),p[1],p[2]));
			}
			else if (s.equals("trfo")){
				System.out.println(String.format(protocolo.get(s),p[1]));
			}
			else{
				System.out.print(protocolo.get(s));
			}
		}
		else{
			System.out.print(s);
		}
	}
	
	public static boolean check(String s){//verificar se o utilizador quer sair
		if (s.equalsIgnoreCase("exit")){
			System.out.println("O cliente abandonou o jogo!");
			return false;
		}
		else{
			return true;
		}
	}
	
	public static String[] readMessage(String message){//separar convenientemente as várias partes de uma mensagem (se houver várias partes)
		String[] list=message.split("&");
		return list;
	}
}
			
			