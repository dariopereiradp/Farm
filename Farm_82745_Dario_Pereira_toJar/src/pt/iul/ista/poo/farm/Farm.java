package pt.iul.ista.poo.farm;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import pt.iul.ista.poo.farm.objects.Animal;
import pt.iul.ista.poo.farm.objects.Chicken;
import pt.iul.ista.poo.farm.objects.FarmObject;
import pt.iul.ista.poo.farm.objects.Farmer;
import pt.iul.ista.poo.farm.objects.Interactable;
import pt.iul.ista.poo.farm.objects.KeyFarmObject;
import pt.iul.ista.poo.farm.objects.Land;
import pt.iul.ista.poo.farm.objects.Sheep;
import pt.iul.ista.poo.farm.objects.Updatable;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class Farm implements Observer {


	private static final int MIN_X = 5;
	private static final int MIN_Y = 5;

	private static Farm INSTANCE;

	private int max_x;
	private int max_y;

	private Farmer farmer = new Farmer (new Point2D (0,0));;
	private Map<KeyFarmObject,FarmObject> objects = new HashMap<>();
	private boolean action = false;
	private int pontos;
	boolean modificou = false;

	public boolean isModificou() {
		return modificou;
	}

	Object[] options = { "Sim", "Não","Cancelar" };
	Object[] options1 = { "Sim", "Não" };
	

	File progress = new File("progress");

	String infor = "\n Farm v.2.0.1.5 - Novidades: menu de ajuda; opção de salvar o jogo ao fechar; melhorias e correção de erros.\n"
			+ "Corrigido o erro que impedia carregar jogos salvos com dimensão maior que 9x9 (v.2.0.1.4)\n"
			+ "Projeto de POO - ISCTE-IUL, por Dário Pereira\n"
			+ "\nPodes mover o agricultor usando as teclas de direção.\n"
			+ "Para fazer uma ação (isto é, cuidar das ovelhas, apanhar um ovo \nou uma galinha,"
			+ " lavar, plantar e colher) dever apertar a tecla 'espaço' seguido \nda tecla de direção correspondente à posição onde queres interagir."
			+ "\nO objetivo é ganhar pontos cuidando da quinta. Cada tecla de direção que apertas conta"
			+ "\ncomo um ciclo do jogo. Para as ovelhas ficarem bem alimentadas deves cuidar delas no mínimo"
			+ "\na cada 10 ciclos. A cada ciclo que elas estão bem alimentadas ganhas 1 ponto. As ovelhas nunca morrem,"
			+ "\nmas, se ficarem mais de 10 ciclos sem comer começam a mover-se aleatoriamente e podem comer tuas plantas.\n"
			+ "Se ficarem 20 ciclos sem comer, ficam famintas e param de se mover até que as alimentes outra vez. \n"
			+ "A galinha move-se aleatoriamente pela quinta e, se encontrar um tomate, come-o. Põe um ovo a cada 10 ciclos."
			+ "\nSe não apanhares o ovo, ao fim de 20 ciclos nasce uma nova galinha.\n"
			+ "Podes ter dois tipos de vegetais: tomates e couves. Para plantar deves primeiro lavrar a terra.\n"
			+ "Quando interagires com uma terra lavrada vai surgir um vegetal aleatório. A couve amadurece\n"
			+ "em 10 ciclos e apodrece em 30. Se cuidares dela, ela cresce mais rápido. Quando estiver madura pode colher"
			+ "\ne ganhas 2 pontos com isso. O tomate amadurece com 15 ciclos e apodrece com 25. Mas só vai amadurecer"
			+ "\nse tiveres cuidado dele pelo menos uma vez. Ao colher ganhas 3 pontos. Atenção: só ganhas pontos ao colher um\n"
			+ "vegetal maduro. Se estiver podre podes limpar o terreno, mas não vais ganhar pontos por isso."
			+ "\nSó pode haver no máximo um vegetal em cada posição. Não podes plantar em terreno rochoso.\n"
			+ "Quando existem na mesma posição um animal (ou ovo) e um vegetal, o alvo da interação do agricultor\n"
			+ "será o animal (ou ovo). Animais e ovos não ocupam a mesma posição e o agricultor não pode passar por cima deles.\n"
			+ "No entando podem passar por cima de vegetais e de qualquer tipo de terreno.\n"
			+ "\nPodes salvar o progresso apertando a tecla 's' e voltar a jogar o jogo salvo apertando a tecla 'l'.\n"
			+ "Como viste podes definir a dimensão da tua quinta (entre 5x5 e 27x13). Mas tem atenção que quanto maior for, mais difícil será!\n"
			+ "Para sair aperta a tecla 'esc'."
			+ "Se tiveres alguma dúvida podes sempre voltar a ver essa mensagem apertando 'F1' a qualquer momento.\n"
			+ "\n<html><body><p width='200px' align='center'>Desejamos um ótimo jogo!";

	/**
	 * 
	 * @param max_x
	 * @param max_y
	 */
	private Farm(int max_x, int max_y) {
		if (max_x < MIN_X || max_y < MIN_Y)
			throw new IllegalArgumentException();

		this.max_x = max_x;
		this.max_y = max_y;

		INSTANCE = this;

		ImageMatrixGUI.setSize(max_x, max_y);

		// Nao usar ImageMatrixGUI antes de inicializar o tamanho		

		loadScenario();
		if(!progress.exists()){
			JOptionPane.showMessageDialog(null,
					"<html><body><p width='200px' align='center'>Bem vindo à quinta!" + 
							infor, "Bem vindo!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("farmer.png")));
			try {
				progress.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE,new ImageIcon(getClass().getResource("farmer.png")));
			}
		}

		if(progress.exists() && progress.length()!=0){
			int opcao = JOptionPane.showOptionDialog(ImageMatrixGUI.getInstance().getPanel(), "Existe um jogo salvo. Deseja continuar de onde estava?\n(Pode sempre continuar mais tarde, apertando a tecla 'l')", "Carregar jogo salvo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon (getClass().getResource("farmer.png")), options1, options1[0]);
			if(opcao == JOptionPane.YES_OPTION)
				load();
		}

	}

	private void registerAll() {
		List<ImageTile> images = new ArrayList<ImageTile>();

		for(int i=0; i<max_x; i++)
			for(int j=0; j<max_y; j++){
				Land l = new Land (new Point2D(i,j));
				images.add(l);
				objects.put(new KeyFarmObject(l), l);
			}

		images.add(farmer);
		Animal ovelha1 = new Sheep(randomPosition());
		objects.put(new KeyFarmObject(ovelha1),ovelha1);
		//		Animal ovelha2 = new Sheep(randomPosition());
		//		objects.put(new KeyFarmObject(ovelha2),ovelha2);
		Animal galinha1 = new Chicken(randomPosition());
		objects.put(new KeyFarmObject(galinha1),galinha1);
		Animal galinha2 = new Chicken(randomPosition());
		objects.put(new KeyFarmObject(galinha2),galinha2);

		images.add(ovelha1); // images.add(ovelha2); 
		images.add(galinha1); images.add(galinha2);

		ImageMatrixGUI.getInstance().addImages(images);
		ImageMatrixGUI.getInstance().update();
	}

	private void loadScenario() {
		registerAll();
	}

	public Point2D randomPosition(){
		Random r = new Random();
		int x= 0;
		int y= 0;
		Point2D p = null;
		do{
			x = r.nextInt(max_x);
			y = r.nextInt(max_y);
			p = new Point2D(x,y);

		} while (!posicaoDisponivel(p));
		return p;

	}

	public boolean posicaoDisponivel (Point2D p){
		return !(p.equals(farmer.getPosition()) || objects.containsKey(new KeyFarmObject(p,2)) || objects.containsKey(new KeyFarmObject(p,3)));
	}

	public boolean temPlanta(Point2D p){
		return objects.containsKey(new KeyFarmObject(p,1));
	}

	public void addObject (FarmObject f){
		objects.put(new KeyFarmObject(f), f);
	}

	public FarmObject getObject (KeyFarmObject key){
		return objects.get(key);
	}

	public void removeObject (KeyFarmObject key){
		objects.remove(key);	
	}


	@SuppressWarnings("rawtypes")
	public void atualizarCiclos (){
		List<Animal> animais = new ArrayList<>();
		Iterator it = objects.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry key = (Map.Entry) it.next();
			if(key.getValue() instanceof Updatable){
				Updatable obj = (Updatable) key.getValue();
				if(obj instanceof Animal)
					animais.add((Animal) obj);
				else
					obj.update();
			}
		}
		for(Animal a: animais){
			a.update();
		}
	}

	public void interacao(int key){
		if(!farmer.novaPosicao(key).equals(farmer.getPosition())){
			KeyFarmObject k = new KeyFarmObject(farmer.novaPosicao(key),getHighestLayer(key));
			if(objects.get(k) instanceof Interactable){
				Interactable obj = (Interactable) objects.get(k);
				obj.interagir();
				action = false;
			}
		}
	}

	public int getHighestLayer(int key){
		if(objects.containsKey(new KeyFarmObject(farmer.novaPosicao(key),3)))
			return 3;
		else if(objects.containsKey(new KeyFarmObject(farmer.novaPosicao(key),2)))
			return 2;
		else if(objects.containsKey(new KeyFarmObject(farmer.novaPosicao(key),1)))
			return 1;
		else return 0;
	}

	public void moveFarmer (int key){
		if(key == KeyEvent.VK_SPACE)
			action = true;
		else if(action)
			interacao(key);
		else
			farmer.move(key);
	}

	public void somarPontos (int valor){
		pontos += valor;
	}

	public void exit(){
		if(modificou){
			int opcao = JOptionPane.showOptionDialog(ImageMatrixGUI.getInstance().getPanel(), "Vai sair do jogo. Deseja salvar o progresso?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon (getClass().getResource("farmer.png")), options, options[0]);
			if(opcao == JOptionPane.YES_OPTION){
				save();
				System.exit(0);
			}
			if(opcao == JOptionPane.NO_OPTION)
				System.exit(0);
			if( opcao == JOptionPane.CANCEL_OPTION)
				ImageMatrixGUI.getInstance().getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
		else{
			int opcao = JOptionPane.showOptionDialog(ImageMatrixGUI.getInstance().getPanel(), "Tem certeza que quer sair?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon (getClass().getResource("farmer.png")), options1, options1[0]);
			if(opcao == JOptionPane.YES_OPTION)
				System.exit(0);
			if( opcao == JOptionPane.NO_OPTION)
				ImageMatrixGUI.getInstance().getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
	}

	@Override
	public void update(Observable gui, Object a) {
		//		System.out.println("Update sent " + a);
		if (a instanceof Integer) {
			int key = (Integer) a;
			if(key == KeyEvent.VK_S)
				save();
			else if(key == KeyEvent.VK_L)
				load();
			else if(key == KeyEvent.VK_F1)
				JOptionPane.showMessageDialog(null,
						"<html><body><p width='200px' align='center'>Ajuda" + 
								infor, "Ajuda", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("farmer.png")));
			else if(key == KeyEvent.VK_ESCAPE)
				exit();				
			else{
				modificou=true;
				moveFarmer(key);
				if(Direction.isDirection(key))
					atualizarCiclos();
			}
		}
		ImageMatrixGUI.getInstance().setStatusMessage("Points: " + pontos);
		ImageMatrixGUI.getInstance().update();
	}


	// Nao precisa de alterar nada a partir deste ponto
	private void play() {
		ImageMatrixGUI.getInstance().addObserver(this);
		ImageMatrixGUI.getInstance().go();
	}

	public static Farm getInstance() {
		assert (INSTANCE != null);
		return INSTANCE;
	}

	//	private static int[] dimensao (){
	//		int linhas = 0;
	//		int colunas = 0; 
	//		try(Scanner s = new Scanner(new File("dimension.txt"));){
	//			while(s.hasNext()){
	//				linhas = s.nextInt();
	//				colunas = s.nextInt();
	//			}
	//		} catch (FileNotFoundException e) {
	//			System.out.println("Erro ao abrir o ficheiro!");
	//			e.printStackTrace();
	//		}
	//		int[] dim = {linhas,colunas};
	//		return dim;
	//	}

	@SuppressWarnings("rawtypes")
	public void save(){
		Date data = new Date(System.currentTimeMillis());  
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm'h'");
		String info = formatarDate.format(data.getTime());
		try(PrintWriter file = new PrintWriter(progress);){
			file.println("Jogo salvo em " + info);
			file.println(max_x + " " + max_y);
			file.println(pontos);
			file.println(farmer.toFile());
			Iterator it = objects.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry key = (Map.Entry) it.next();
				FarmObject temp = (FarmObject) key.getValue();
				file.println(temp.toFile());
			}
			modificou=false;
//			System.out.println("O jogo foi salvo com sucesso em " + info);
			JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "O jogo foi salvo com sucesso em  \n" + info, "Salvar progresso",
					JOptionPane.INFORMATION_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
		} catch (FileNotFoundException e) {
			System.out.println("Erro ao criar o ficheiro!");
			JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "Erro ao salvar o jogo", "Salvar progresso",
					JOptionPane.ERROR_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
		}

	}

	public void load(){
		Map<KeyFarmObject,FarmObject> temp = new HashMap<>();
		List<ImageTile> tempImg = new ArrayList<>();
		try(Scanner s = new Scanner(progress);){
			String info = s.nextLine();
			if(s.nextInt() != max_x || s.nextInt() != max_y){
				JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "Impossível carregar o jogo gravado. \n Dimensão diferente da original!", "Carregar jogo salvo",
						JOptionPane.ERROR_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
				//System.out.println("Impossivel carregar o jogo gravado: dimensão diferente!");
			}
			else{
				pontos = s.nextInt();
				s.nextLine();
				farmer = (Farmer) FarmObject.newFarmObject(s.nextLine().split(";"));
				while(s.hasNextLine()){
					String[] tokens = s.nextLine().split(";");
					FarmObject f = FarmObject.newFarmObject(tokens);
					temp.put(new KeyFarmObject(f), f);
					tempImg.add(f);
				}
				ImageMatrixGUI.getInstance().clearImages();
				objects = temp;
				ImageMatrixGUI.getInstance().addImages(tempImg);
				ImageMatrixGUI.getInstance().addImage(farmer);
				JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "O " + info + " \n foi carregado com sucesso!", "Carregar jogo salvo",
						JOptionPane.INFORMATION_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
				//System.out.println("O " + info + " foi carregado com sucesso!");
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "Não foi encontrado nenhum jogo salvo! Salva primeiro o jogo atual.", "Carregar jogo salvo",
					JOptionPane.ERROR_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
			//System.out.println("Erro ao ler o ficheiro!");
		} catch (IllegalArgumentException e){
			JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "Erro! Impossível carregar o jogo salvo!\nFicheiro corrompido ou modificado.", "Carregar jogo salvo",
					JOptionPane.ERROR_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
		} catch (NoSuchElementException e){
			JOptionPane.showMessageDialog(ImageMatrixGUI.getInstance().getPanel(), "Não foi encontrado nenhum jogo salvo! Salva primeiro o jogo atual.", "Carregar jogo salvo",
					JOptionPane.ERROR_MESSAGE , new ImageIcon (getClass().getResource("farmer.png")) );
		}
	}

	public static void main(String[] args) {

		boolean b=false;
		int x=0;
		int y=0;
		do{
			try{
				x = Integer.parseInt(JOptionPane.showInputDialog(null, "Informe a largura (entre 5 e 27): ", "Dimensão", JOptionPane.QUESTION_MESSAGE));
				y = Integer.parseInt(JOptionPane.showInputDialog(null, "Informe a altura (entre 5 e 13): ", "Dimensão", JOptionPane.QUESTION_MESSAGE));
				b=true;
			} catch (NumberFormatException e){
				b=false;
			}
		}	while(!b || x<5 || y<5 || x>27 || y>13);
		Farm f = new Farm(x,y);
		f.play();



	}
}
