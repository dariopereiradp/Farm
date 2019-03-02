package pt.iul.ista.poo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.utils.Point2D;

/**
 * @author POO 2016
 * 
 *         ImageMatrixGUI, manages and displays in its main window a grid of
 *         square images (implementations of ImageTile) of the same size. The
 *         default image-size is 48x48 and the default grid-size is 10x10.
 * 
 *         This class also provides a status-bar (also composed of square images
 *         of the same size) that will be displayed above the grid.
 * 
 *         This class is observable and will send an update message everytime a
 *         key is pressed.
 * 
 *         The images that ImageTiles refer to MUST be in a folder called
 *         "images" directly under the project folder.
 * 
 *         ImageTile is required to provide the name of the image (e.g.
 *         "XxxX") and its position (in tile coordinates, with 0,0 in the top
 *         left corner and increasing to the right in the horizontal axis and
 *         downwards in the vertical axis). ImageMatrixGUI will look for an
 *         image with that name in the "images" folder (e.g. "XxxX.png") and draw
 *         that image in the window.
 * 
 *         ImageMatrixGUI implements the Singleton pattern.
 *
 */

/**
 * @author lmmn
 *
 */
public class ImageMatrixGUI extends Observable {

	private static ImageMatrixGUI INSTANCE;

//	private final String IMAGE_DIR = "src/images";
	private final int LABEL_HEIGHT = 20;
	private final int SQUARE_SIZE;
	//	private final int N_SQUARES_WIDTH;
	//	private final int N_SQUARES_HEIGHT;

	private static int n_squares_width = 3;
	private static int n_squares_height = 3;

	private JFrame frame;
	private JPanel panel;

	public JPanel getPanel() {
		return panel;
	}
	

	public JFrame getFrame() {
		return frame;
	}


	private JLabel info;

	private Map<String, ImageIcon> imageDB = new HashMap<String, ImageIcon>();

	private List<ImageTile> images = new ArrayList<ImageTile>();

	private Point2D lastMouseCoordinate;
	private boolean mouseClicked;

	private int lastKeyPressed;
	private boolean keyPressed;

	private int maxLevel;

	private ImageMatrixGUI() {
		SQUARE_SIZE = 50;
		//		N_SQUARES_WIDTH = 10;
		//		N_SQUARES_HEIGHT = 10;
		init();
	}

	/**
	 * @return Access to the Singleton instance of ImageMatrixGUI
	 */
	public static ImageMatrixGUI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ImageMatrixGUI();
		return INSTANCE;
	}

	/**
	 * 
	 * Setter for the name of the frame
	 * 
	 * @param name
	 *            Name of application (will be displayed as a frame title in the
	 *            top left corner)
	 * 
	 */

	public void setName(final String name) {
		frame.setTitle(name); // Corrected 2-Mar-2016
	}

	private void init() {
		frame = new JFrame();
		panel = new DisplayWindow();
		info = new JLabel();


		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Farm.getInstance().exit();
			}
		});
		frame.setIconImage((Toolkit.getDefaultToolkit().getImage((getClass().getResource("farmer.png")))));
		frame.setTitle("Farm v.2.0 by @D++ Dário Pereira");
		panel.setPreferredSize(new Dimension(n_squares_width * SQUARE_SIZE, n_squares_height * SQUARE_SIZE));
		info.setPreferredSize(new Dimension(n_squares_width * SQUARE_SIZE, LABEL_HEIGHT));
		//		panel.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, N_SQUARES_HEIGHT * SQUARE_SIZE));
		//		info.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, LABEL_HEIGHT));
		info.setBackground(Color.BLACK);
		frame.add(panel);
		frame.add(info, BorderLayout.NORTH);
		frame.pack();
		frame.setResizable(false); // Added 27-Feb-2018
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initImages();

		new KeyWatcher().start();

		//		new MouseWatcher().start();

		//		new Ticker().start();

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				lastKeyPressed = e.getKeyCode();
				keyPressed = true;
				releaseObserver();
			}
		});

		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				lastMouseCoordinate = new Point2D(e.getPoint());
				mouseClicked = true;
				releaseObserver();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
	}

	synchronized void releaseObserver() {
		notifyAll();
	}

	synchronized void waitForKey() throws InterruptedException {
		while (!keyPressed) {
			wait();
		}
		setChanged();
		notifyObservers(lastKeyPressed);
		keyPressed = false;
	}

	synchronized void waitForClick() throws InterruptedException {
		while (!mouseClicked) {
			wait();
		}
		setChanged();
		notifyObservers(lastMouseCoordinate);
		mouseClicked = false;
	}

	void tick() throws InterruptedException {
		setChanged();
		notifyObservers();
	}

	private void initImages() {
		//		File dir = new File(IMAGE_DIR);
		//		for (File f : dir.listFiles()) {
		//			assert (f.getName().lastIndexOf('.') != -1);
		//			imageDB.put(f.getName().substring(0, f.getName().lastIndexOf('.')),
		//					new ImageIcon(getClass().getResource(f.getName())));
		//		}
		imageDB.put("bad_cabbage.png".substring(0, "bad_cabbage.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("bad_cabbage.png")));
		imageDB.put("bad_tomato.png".substring(0, "bad_tomato.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("bad_tomato.png")));
		imageDB.put("cabbage.png".substring(0, "cabbage.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("cabbage.png")));
		imageDB.put("tomato.png".substring(0, "tomato.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("tomato.png")));
		imageDB.put("chicken.png".substring(0, "chicken.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("chicken.png")));
		imageDB.put("egg.png".substring(0, "egg.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("egg.png")));
		imageDB.put("famished_sheep.png".substring(0, "famished_sheep.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("famished_sheep.png")));
		imageDB.put("farmer.png".substring(0, "farmer.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("farmer.png")));
		imageDB.put("land.png".substring(0, "land.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("land.png")));
		imageDB.put("plowed.png".substring(0, "plowed.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("plowed.png")));
		imageDB.put("rock.png".substring(0, "rock.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("rock.png")));
		imageDB.put("sheep.png".substring(0, "sheep.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("sheep.png")));
		imageDB.put("small_tomato.png".substring(0, "small_tomato.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("small_tomato.png")));
		imageDB.put("small_cabbage.png".substring(0, "small_cabbage.png".lastIndexOf('.')),
				new ImageIcon(getClass().getResource("small_cabbage.png")));

	}

	/**
	 * 
	 * Make the window visible.
	 * 
	 */
	public void go() {
		frame.setVisible(true);
	}

	/**
	 * 
	 * Add a new set of images to the main window.
	 * 
	 * @param newImages
	 *            images to be added to main window
	 * 
	 * @throws IllegalArgumentException
	 *             if no image with that name (and a suitable extension) is
	 *             found the images folder
	 * 
	 */

	public void addImages(final List<ImageTile> newImages) {
		synchronized (images) { // Added 16-Mar-2016
			if (newImages == null)
				throw new IllegalArgumentException("Null list");
			if (newImages.size() == 0)
				return;
			for (ImageTile i : newImages) {
				if (i == null)
					throw new IllegalArgumentException("Null image");
				if (!imageDB.containsKey(i.getName())) {
					throw new IllegalArgumentException("No such image in DB " + i.getName());
				}
				addImage(i);
			}
		}
	}

	// Added 2-Mar-2016
	/**
	 * Removes the image given as a parameter.
	 * 
	 * Does nothing if there is no match.
	 * 
	 * @param image
	 *            to be removed (must be the exact same Object and not a copy)
	 * 
	 */

	public void removeImage(final ImageTile image) {
		if (image == null)
			throw new IllegalArgumentException("Null list");
		synchronized (images) { // Added 16-Mar-2016
			images.remove(image);
		}
	}

	// Added 2-Out-2017
	/**
	 * Removes the image given as a parameter.
	 * 
	 * Does nothing if there is no match.
	 * 
	 * @param image
	 *            to be removed (must be the exact same Object and not a copy)
	 * 
	 */

	public void removeImages(final List<ImageTile> newImages) {
		if (newImages == null)
			throw new IllegalArgumentException("Null list");
		synchronized (images) {
			images.removeAll(newImages);
		}
	}

	// Added 2-Mar-2016
	/**
	 * Adds image to main window
	 * 
	 * @param image
	 *            to be added
	 */
	public void addImage(final ImageTile image) {
		synchronized (images) { // Added 16-Mar-2016
			if (image == null)
				throw new IllegalArgumentException("Null image");
			if (image.getName() == null)
				throw new IllegalArgumentException("Null image name");
			if (image.getPosition() == null)
				throw new IllegalArgumentException("Null image position");
			if (image.getLayer() >= maxLevel)
				maxLevel = image.getLayer() + 1;
			if (!imageDB.containsKey(image.getName())) {
				throw new IllegalArgumentException("No such image in DB " + image.getName());
			}
			images.add(image);
		}
	}

	/**
	 * Clear all images displayed in main window.
	 */
	public void clearImages() {
		synchronized (images) { // Added 16-Mar-2016
			images.clear();
		}
	}

	/**
	 * 
	 * Add a new set of images to the status window.
	 * 
	 * @param newImages
	 *            images to be added to status bar
	 *
	 * @throws IllegalArgumentException
	 *             if no image with that name (and a suitable extension) is
	 *             found the images folder
	 *
	 * 
	 */

	public void setStatusMessage(String message) {
		info.setText(message);
	}

	@SuppressWarnings("serial") // Added 2-Mar-2016
	private class DisplayWindow extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			// System.out.println("Thread " + Thread.currentThread() + "
			// repainting");
			synchronized (images) { // Added 16-Mar-2016
				for (int j = 0; j != maxLevel; j++)
					for (ImageTile i : images) {
						if (i.getLayer() == j) {
							g.drawImage(imageDB.get(i.getName()).getImage(), i.getPosition().getX() * SQUARE_SIZE,
									i.getPosition().getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, frame);
						}
					}
			}
		}
	}

	private class KeyWatcher extends Thread {
		public void run() {
			try {
				while (true)
					waitForKey();
			} catch (InterruptedException e) {
			}
		}
	}

	//	private class MouseWatcher extends Thread {
	//		public void run() {
	//			try {
	//				while (true)
	//					waitForClick();
	//			} catch (InterruptedException e) {
	//			}
	//		}
	//	}
	//
	//	private class Ticker extends Thread {
	//		private static final long TICK_TIME = 1000;
	//
	//		public void run() {
	//			try {
	//				while (true) {
	//					sleep(TICK_TIME);
	//					tick();
	//				}
	//			} catch (InterruptedException e) {
	//			}
	//		}
	//	}

	/**
	 * Force scheduling of a new window paint (this may take a while, it does
	 * not necessarily happen immediately after this instruction is issued)
	 */
	public void update() {
		frame.repaint();
	}

	/**
	 * Terminate window GUI
	 */
	public void dispose() {
		images.clear();
		imageDB.clear();
		frame.dispose();
	}

	/**
	 * 
	 * Grid dimensions
	 * 
	 * @return the width and height of the image grid
	 * 
	 */
	public Dimension getGridDimension() {
		//		return new Dimension(N_SQUARES_WIDTH, N_SQUARES_HEIGHT);
		return new Dimension(n_squares_width, n_squares_height);
	}

	public static void setSize(int i, int j) {
		n_squares_width = i;
		n_squares_height = j;
	}

	public boolean isWithinBounds(Point2D p) {
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < n_squares_width && p.getY() < n_squares_height;
	}

}
