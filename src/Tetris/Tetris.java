package Tetris;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *                                                                                                                  
 * This class runs the <code>Tetris.java</code> program.              
 *                                                                                                                     
 * @author Timothy Xu <thx35333@uga.edu>                                                               
 */
public class Tetris {
	boolean first = true;
	boolean lost = false;
	private int row = -1;
	private int col = 4;
	private int numLinesCleared = 0;
	private int score = 0;
	private int level = 1;
	private int orientation = 0;
	private boolean line = false;
	private boolean square = false;
	private boolean Hline = true;
	private boolean lShape = false;
	private boolean jShape = false;
	private boolean tShape = false;
	private boolean zShape = false;
	private boolean sShape = false;
	Label label = new Label("Level: " + level + "\nScore: " + score + "\nLines: " + numLinesCleared);

	/**
	 * Starts the tetris game.
	 */
	public void startTetris() {
		HBox hbox = new HBox();

		// creates scoreboard on right
		VBox scoreBoard = new VBox(100);
		Image tetrisImage = new Image("file:tetris2.jpg");
		ImageView tetrisView = new ImageView(tetrisImage);
		tetrisView.setFitHeight(130);
		tetrisView.setFitWidth(190);

		label.setPadding(new Insets(40));
		scoreBoard.getChildren().addAll(tetrisView, label);

		GridPane gridpane = new GridPane();
		// Creates Grid
		Rectangle[][] board = new Rectangle[22][10];
		for (int row = 0; row < 22; row++) {
			for (int col = 0; col < 10; col++) {
				board[row][col] = new Rectangle(30, 30, Color.BLACK);
				board[row][col].setStroke(Color.GREY);
				GridPane.setConstraints(board[row][col], col, row);// adds it to a specific row and column
				gridpane.getChildren().add(board[row][col]);
			}
		}

		newShape(gridpane, board);
		first = false;
		startLoop(gridpane, board);

		// sets the scene and stage
		hbox.getChildren().addAll(gridpane, scoreBoard);
		Scene scene = new Scene(hbox, 500, 682);
		Stage stage = new Stage();
		stage.setTitle("Tetris");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	/**
	 * Checks to see if the space below the grid is empty.
	 * 
	 * @param board the board
	 * @return true if the shape can continue to fall
	 */
	private boolean timelineEmptyDown(Rectangle[][] board) {
		if (line) {
			if (Hline) {
				for (int j = col; j <= col + 3; j++) {
					if (row == 22 || !(board[row][j].getFill().equals(Color.BLACK))) {
						return false;
					}
				}
				return true;
			}

			else {
				// if it hits the bottom, reset it to the center
				// TODO error may be here with the ==22 because the array is 0-21 look into it
				if (row + 3 == 22 || !(board[row + 3][col].getFill().equals(Color.BLACK))) {
					return false;
				}
				return true;
			}
		} else if (square) {
			for (int j = col; j <= col + 1; j++) {
				if (row + 1 == 22 || !(board[row + 1][j].getFill().equals(Color.BLACK))) {
					return false;
				}
			}
			return true;
		} else if (lShape) {

			if (orientation == 0) {
				if (row + 2 == 22 || !(board[row + 2][col].getFill().equals(Color.BLACK))
						|| !(board[row][col - 1].getFill().equals(Color.BLACK))) {

					return false;
				}
				return true;
			}
			if (orientation == 1) {
				for (int j = col - 1; j < col + 2; j++) {
					if (row == 22 || !(board[row][j].getFill().equals(Color.BLACK))) {

						return false;
					}
				}
				return true;
			}
			// here
			if (orientation == 2) {

				if (row + 1 == 22 || !(board[row + 1][col].getFill().equals(Color.BLACK))) {
					return false;
				}
				if (!(board[row + 1][col + 1].getFill().equals(Color.BLACK))) {
					return false;
				}
				return true;
			}
			if (orientation == 3) {
				for (int j = col; j < col + 2; j++) {
					if (row + 1 == 22 || !(board[row][j].getFill().equals(Color.BLACK))) {
						return false;
					}
				}
				if (!(board[row + 1][col - 1].getFill().equals(Color.BLACK))) {
					return false;
				}
				return true;
			}

		} else if (jShape) {
			if (orientation == 0) {
				if (row + 2 == 22 || !(board[row + 2][col].getFill().equals(Color.BLACK))
						|| !(board[row][col + 1].getFill().equals(Color.BLACK))) {
					return false;
				}
			} else if (orientation == 1) {
				if (row + 1 == 22 || !(board[row][col].getFill().equals(Color.BLACK))
						|| !(board[row][col - 1].getFill().equals(Color.BLACK))
						|| !(board[row + 1][col + 1].getFill().equals(Color.BLACK))) {
					return false;
				}
			} else if (orientation == 2) {
				if (row + 1 == 22 || !(board[row + 1][col].getFill().equals(Color.BLACK))
						|| !(board[row + 1][col - 1].getFill().equals(Color.BLACK))) {
					return false;
				}
			} else if (orientation == 3) {
				if (row == 22 || !(board[row][col].getFill().equals(Color.BLACK))
						|| !(board[row][col - 1].getFill().equals(Color.BLACK))
						|| !(board[row][col + 1].getFill().equals(Color.BLACK))) {
					return false;
				}
			}
		}
		else if (tShape) {
			if (orientation == 0) {
				if (row + 1 == 22 || !(board[row + 1][col].getFill().equals(Color.BLACK))
						|| !(board[row+1][col - 1].getFill().equals(Color.BLACK))||
						!(board[row+1][col + 1].getFill().equals(Color.BLACK))) {

					return false;
				}
				return true;
			}
		}
		else if (zShape) {
			if (orientation == 0) {
				if (row + 1 == 22 || !(board[row + 1][col].getFill().equals(Color.BLACK))
						|| !(board[row][col - 1].getFill().equals(Color.BLACK))||
						!(board[row+1][col + 1].getFill().equals(Color.BLACK))) {
					return false;
				}
				return true;
			}
		} 
		else if (sShape) {

			if (orientation == 0) {
				if (row + 1 == 22 || !(board[row + 1][col].getFill().equals(Color.BLACK))
						|| !(board[row+1][col - 1].getFill().equals(Color.BLACK))||
						!(board[row][col + 1].getFill().equals(Color.BLACK))) {

					return false;
				}
				return true;
			}
		}	
		return true;
	}

	/**
	 * clears the line and shifts everything down and updates the score
	 * 
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void clearLine(GridPane gridpane, Rectangle[][] board) {
		boolean cleared = false;
		boolean firstBlock = true;
		int count = 0;		
		int clearedRow = 0;
		for (int i = 0; i < 22; i++) {
			boolean fullRow = true;
			for (int j = 0; j < 10; j++) {
				if (board[i][j].getFill().equals(Color.BLACK)) {// checks if the row is full or not
					fullRow = false;
				}
			}
			// TODO update label for score
			// if the row is full, clear it and shift all of the blocks below it down one
			if (fullRow) {
				for (int j = 0; j < 10; j++) {
					board[i][j].setFill(Color.BLACK);
					cleared = true;					
					clearedRow = i;					
					System.out.println(clearedRow);					
				}
				count++;				
				numLinesCleared++;
				score = score + 100;
				if (numLinesCleared == 3 || numLinesCleared == 6 || numLinesCleared == 9 || numLinesCleared == 12
						|| numLinesCleared == 15) {
					level++;
				}
				label.setText("Level: " + level + "\nScore: " + score + "\nLines: " + numLinesCleared);

			}
		}
		if (cleared) {
			for(int r = 0; r< count;r++) {
				for (int l = 0; l < 10; l++) {
					Paint c = Color.BLACK;
					Paint temp = Color.BLACK;
					firstBlock = true;
					for (int k = 0; k < clearedRow; k++) {
						if (firstBlock && !board[k][l].getFill().equals(Color.BLACK)
								&& board[k - 1][l].getFill().equals(Color.BLACK)) {
							c = board[k][l].getFill();
							temp = board[k + 1][l].getFill();
							board[k + 1][l].setFill(c);
							board[k][l].setFill(Color.BLACK);

							firstBlock = false;
						} else if (!board[k][l].getFill().equals(Color.BLACK)) {
							// System.out.println("row: " +k +" col: " + l);
							c = temp;
							if(!board[k+1][l].getFill().equals(Color.BLACK)) {
								temp = board[k + 1][l].getFill();
							}
							
							board[k + 1][l].setFill(c);
						}
					}
				}
			}			
		}
	}

	/**
	 * Starts the loop. Makes each shape move down one
	 * 
	 * @param board the board
	 */
	public void startLoop(GridPane gridpane, Rectangle[][] board) {
		EventHandler<ActionEvent> handler = event1 -> {
			if (lost == false) {
				if (line) {
					// makes the pieces go down
					row++;

					if (Hline) {
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							line = false;
							clearLine(gridpane, board);
							newShape(gridpane, board);
						} else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col + 1].setFill(Color.BLACK);
								board[row - 1][col + 2].setFill(Color.BLACK);
								board[row - 1][col + 3].setFill(Color.BLACK);
							}
							line(gridpane, board);
						}
					} else {
						// if it hits the bottom, reset it to the center
						
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							line = false;
							clearLine(gridpane, board);
							newShape(gridpane, board);
						} else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
							}
							line(gridpane, board);
						}
					}
				}

				else if (square) {
					row++;

					// if it hits the bottom
					if (!timelineEmptyDown(board)) {
						row = -1;
						col = 4;
						square = false;
						clearLine(gridpane, board);
						newShape(gridpane, board);
					} else {
						if (row != 0) {
							board[row - 1][col].setFill(Color.BLACK);
							board[row - 1][col + 1].setFill(Color.BLACK);
						}
						square(gridpane, board);
					}

				} 
				else if (lShape) {
					row++;
					if (orientation == 0) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							lShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} else {
							if (row != 0) {
								System.out.println(orientation);
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col - 1].setFill(Color.BLACK);
							}
							lShape(gridpane, board);
						}
					}
					// here
					if (orientation == 1) {

						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							lShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} 
						else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col - 1].setFill(Color.BLACK);
								board[row - 2][col + 1].setFill(Color.BLACK);

							}
							lShape(gridpane, board);
						}
					}
					if (orientation == 2) {

						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							lShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} 
						else {
							if (row != 0) {
								board[row - 2][col].setFill(Color.BLACK);
								board[row][col + 1].setFill(Color.BLACK);
							}
							lShape(gridpane, board);
						}
					}
					if (orientation == 3) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							lShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col - 1].setFill(Color.BLACK);
								board[row - 1][col + 1].setFill(Color.BLACK);
							}
							lShape(gridpane, board);
						}
					}

				} 
				else if (jShape) {
					row++;
					if (orientation == 0) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							jShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col + 1].setFill(Color.BLACK);
							}
							jShape(gridpane, board);
						}
					} 
					else if (orientation == 1) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							jShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 1][col + 1].setFill(Color.BLACK);
								board[row - 1][col - 1].setFill(Color.BLACK);
							}
							jShape(gridpane, board);
						}
					} 
					else if (orientation == 2) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							jShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} else {
							if (row != 0) {
								board[row - 2][col].setFill(Color.BLACK);
								board[row][col - 1].setFill(Color.BLACK);
							}
							jShape(gridpane, board);
						}
					} 
					else if (orientation == 3) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							jShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						}
						else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row - 2][col - 1].setFill(Color.BLACK);
								board[row - 1][col + 1].setFill(Color.BLACK);
							}
							jShape(gridpane, board);
						}
					}
				}
				else if(tShape) {
					row++;
					if (orientation == 0) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							tShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} 
						else {
							if (row != 0) {
								System.out.println(orientation);
								board[row - 1][col].setFill(Color.BLACK);
								board[row][col - 1].setFill(Color.BLACK);
								board[row][col + 1].setFill(Color.BLACK);
								
							}
							tShape(gridpane, board);
						}
					}
				}
				else if(zShape) {
					row++;
					if (orientation == 0) {
						// if it hits the bottom
						if (!timelineEmptyDown(board))  {
							row = -1;
							col = 4;
							zShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} 
						else {
							if (row != 0) {
								board[row - 1][col].setFill(Color.BLACK);
								board[row-1][col - 1].setFill(Color.BLACK);
								board[row][col].setFill(Color.BLACK);
								board[row][col + 1].setFill(Color.BLACK);
							}
							zShape(gridpane, board);
						}
					}
				}
				else if(sShape) {
					row++;
					if (orientation == 0) {
						// if it hits the bottom
						if (!timelineEmptyDown(board)) {
							row = -1;
							col = 4;
							sShape = false;
							clearLine(gridpane, board);
							newShape(gridpane, board); // test
						} 
						else {
							if (row != 0) {
								System.out.println(orientation);
								board[row - 1][col].setFill(Color.BLACK);
								board[row-1][col + 1].setFill(Color.BLACK);
								board[row][col - 1].setFill(Color.BLACK);
								
							}
							sShape(gridpane, board);
						}
					}
				}

			}
		};

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(.3), handler);
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}

	/**
	 * Creates a new shape in the grid.
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void newShape(GridPane gridpane, Rectangle[][] board) {
		String s = "";
		orientation = 0;
		Random r = new Random();
		int a = r.nextInt(7);
		if (a == 0) {
			Hline = true;
			line = true;
			square = false;
			lShape = false;
			jShape = false;
			tShape = false;
			s = "line";
		} else if (a == 1) {
			line = false;
			square = true;
			lShape = false;
			jShape = false;
			tShape = false;
			s = "square";
		} else if (a == 2) {
			line = false;
			square = false;
			lShape = true;
			jShape = false;
			tShape = false;
			s = "lShape";
		} else if (a == 3) {
			line = false;
			square = false;
			lShape = false;
			jShape = true;
			tShape = false;
			s = "jShape";
		}
		else if (a == 4) {
			line = false;
			square = false;
			lShape = false;
			jShape = false;
			tShape = true;
			s = "tShape";
		}
		else if (a == 5) {
			line = false;
			square = false;
			lShape = false;
			jShape = false;
			tShape = false;
			zShape = true;
			s = "zShape";
		}
		else if (a == 6) {
			line = false;
			square = false;
			lShape = false;
			jShape = false;
			tShape = false;
			zShape = false;
			sShape = true;
			s = "sShape";
		}

		if (first == false) {
			for (int i = 0; i < 10; i++) {
				if (!board[0][i].getFill().equals(Color.BLACK)) {
					endGame();
				}
			}
		}
		System.out.println(s);
	}

	/**
	 * Line shape for the tetris grid.
	 * 
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void line(GridPane gridpane, Rectangle[][] board) {

		if (Hline) {
			board[row][col].setFill(Color.YELLOW);
			board[row][col + 1].setFill(Color.YELLOW);
			board[row][col + 2].setFill(Color.YELLOW);
			board[row][col + 3].setFill(Color.YELLOW);
		} else {
			board[row][col].setFill(Color.YELLOW);
			board[row + 1][col].setFill(Color.YELLOW);
			board[row + 2][col].setFill(Color.YELLOW);
			board[row + 3][col].setFill(Color.YELLOW);
		}

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			
			boolean emptyLeft = true;
			boolean emptyRight = true;
			boolean emptyDown = true;
			if(row<0) {
				return;
			}
			//checks if the line can go left, right, or down
			if (Hline) {
				for (int i = row; i <= row; i++) {
					if (col == 0 || col != 0 && !board[i][col - 1].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col == 6 || col != 6 && !board[i][col + 4].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
				for (int j = col; j <= col + 3; j++) {
					if (row == 21 || !board[row + 1][j].getFill().equals(Color.BLACK)) {
						emptyDown = false;
					}
				}

			} else {
				System.out.print(row);
				for (int i = row; i <= row + 3; i++) {
					if (col == 0 || col != 0 && !board[i][col - 1].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col == 9 || col != 9 && !board[i][col + 1].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
				if (row + 3 == 21 || !board[row + 4][col].getFill().equals(Color.BLACK)) {
					emptyDown = false;
				}
			}

			boolean canRotate = true;

			if (Hline) {
				if (row > 19) {
					canRotate = false;
				} else {
					for (int i = row + 1; i <= row + 2; i++) {
						if (!board[i][col].getFill().equals(Color.BLACK)) {
							canRotate = false;
						}
					}
				}

			} else {

				if (col == 0 || col != 0 && !board[row + 1][col - 1].getFill().equals(Color.BLACK)) {
					canRotate = false;
				}
				if (col > 7 || col != 8 && !board[row + 1][col + 1].getFill().equals(Color.BLACK)) {
					canRotate = false;
				}
				if (col > 7 || col != 8 && !board[row + 1][col + 2].getFill().equals(Color.BLACK)) {
					canRotate = false;
				}

			}
			//moves line left
			if (event.getCode() == KeyCode.LEFT) {

				if (Hline) {
					if (col > 0 && emptyLeft) {
						board[row][col - 1].setFill(Color.YELLOW);
						board[row][col + 3].setFill(Color.BLACK);

						col--;
					}
				} else {
					if (col > 0 && emptyLeft) {
						board[row][col - 1].setFill(Color.YELLOW);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col - 1].setFill(Color.YELLOW);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col - 1].setFill(Color.YELLOW);
						board[row + 2][col].setFill(Color.BLACK);
						board[row + 3][col - 1].setFill(Color.YELLOW);
						board[row + 3][col].setFill(Color.BLACK);
						col--;
					}
				}

			}
			//moves line right
			if (event.getCode() == KeyCode.RIGHT) {

				if (Hline) {
					if (col < 6 && emptyRight) {
						board[row][col + 4].setFill(Color.YELLOW);
						board[row][col].setFill(Color.BLACK);

						col++;
					}
				} else {
					if (col < 9 && emptyRight) {
						board[row][col + 1].setFill(Color.YELLOW);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col + 1].setFill(Color.YELLOW);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col + 1].setFill(Color.YELLOW);
						board[row + 2][col].setFill(Color.BLACK);
						board[row + 3][col + 1].setFill(Color.YELLOW);
						board[row + 3][col].setFill(Color.BLACK);
						col++;
					}
				}

			}
			// rotates line when up key is pressed
			if (event.getCode() == KeyCode.UP) {

				if (canRotate) {
					rotateLine(board);
				}

			}

			// quickly moves line down when the down key is pressed
			if (event.getCode() == KeyCode.DOWN) {
				if (Hline && emptyDown && row != 22) {
					board[row + 1][col].setFill(Color.YELLOW);
					board[row + 1][col + 1].setFill(Color.YELLOW);
					board[row + 1][col + 2].setFill(Color.YELLOW);
					board[row + 1][col + 3].setFill(Color.YELLOW);
					board[row][col].setFill(Color.BLACK);
					board[row][col + 1].setFill(Color.BLACK);
					board[row][col + 2].setFill(Color.BLACK);
					board[row][col + 3].setFill(Color.BLACK);
					row++;
				} else if (!Hline && emptyDown && row < 18) {
					board[row + 4][col].setFill(Color.YELLOW);
					board[row][col].setFill(Color.BLACK);
					row++;
				}
			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
	}

	/**
	 * rotates the line
	 */
	public void rotateLine(Rectangle[][] board) {
		if (Hline) {
			board[row][col].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			board[row][col + 2].setFill(Color.BLACK);
			board[row][col + 3].setFill(Color.BLACK);

			row = row - 2;
			col++;
			Hline = false;
		} else if (col > 0 && col < 8) {
			board[row][col].setFill(Color.BLACK);
			board[row + 2][col].setFill(Color.BLACK);
			board[row + 3][col].setFill(Color.BLACK);
			board[row + 1][col].setFill(Color.BLACK);

			row = row + 1;
			col--;
			Hline = true;
		}
	}
	/**
	 * creates square shape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void square(GridPane gridpane, Rectangle[][] board) {
		board[row][col].setFill(Color.GREEN);
		board[row + 1][col].setFill(Color.GREEN);
		board[row][col + 1].setFill(Color.GREEN);
		board[row + 1][col + 1].setFill(Color.GREEN);

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			System.out.println("row "+row);
			boolean emptyLeft = true;
			boolean emptyRight = true;
			boolean emptyDown = true;
			if(row<0) {
				return;
			}
			for (int i = row; i < row + 2; i++) {
				if (i != -1 && col != 0 && !board[i][col - 1].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (i != -1 && col != 8 && !board[i][col + 2].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			}
			for (int j = col; j < col + 2; j++) {
				if (row == 20 || !board[row + 2][j].getFill().equals(Color.BLACK)) {
					emptyDown = false;
				}
			}
			//moves left
			if (event.getCode() == KeyCode.LEFT) {
				if (col != 0 && emptyLeft) {
					board[row][col - 1].setFill(Color.GREEN);
					board[row + 1][col - 1].setFill(Color.GREEN);
					board[row][col + 1].setFill(Color.BLACK);
					board[row + 1][col + 1].setFill(Color.BLACK);
					col--;
				}
			}
			//moves right
			if (event.getCode() == KeyCode.RIGHT) {
				if (col < 8 && col != 8 && emptyRight) {
					board[row][col + 2].setFill(Color.GREEN);
					board[row + 1][col + 2].setFill(Color.GREEN);
					board[row][col].setFill(Color.BLACK);
					board[row + 1][col].setFill(Color.BLACK);
					col++;
				}
			}
			// moves square down when the down key is pressed
			if (event.getCode() == KeyCode.DOWN) {

				if (row < 20 && emptyDown) {
					board[row + 2][col].setFill(Color.GREEN);
					board[row + 2][col + 1].setFill(Color.GREEN);
					board[row][col].setFill(Color.BLACK);
					board[row][col + 1].setFill(Color.BLACK);
					row++;
				}

			}

			// TODO bounds checking
		});
		gridpane.requestFocus();
	}
	/**
	 * creates the lshape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void lShape(GridPane gridpane, Rectangle[][] board) {

		if (orientation == 0) {
			board[row][col].setFill(Color.BLUE);
			board[row][col - 1].setFill(Color.BLUE);
			board[row + 1][col].setFill(Color.BLUE);
			board[row + 2][col].setFill(Color.BLUE);

		} else if (orientation == 1) {
			board[row][col].setFill(Color.BLUE);
			board[row - 1][col + 1].setFill(Color.BLUE);
			board[row][col - 1].setFill(Color.BLUE);
			board[row][col + 1].setFill(Color.BLUE);
		} else if (orientation == 2) {
			board[row][col].setFill(Color.BLUE);
			board[row - 1][col].setFill(Color.BLUE);
			board[row + 1][col].setFill(Color.BLUE);
			board[row + 1][col + 1].setFill(Color.BLUE);
		} else if (orientation == 3) {
			board[row][col].setFill(Color.BLUE);
			board[row][col - 1].setFill(Color.BLUE);
			board[row][col + 1].setFill(Color.BLUE);
			board[row + 1][col - 1].setFill(Color.BLUE);
		}

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			
			boolean emptyLeft = true;
			boolean emptyRight = true;
			if(row<0) {
				return;
			}
			//orientations are the state of rotation
			if (orientation == 0) {
				for (int i = row; i < row + 3; i++) {
					if (col - 1 == 0 || !board[i][col - 2].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col == 9 || !board[i][col + 1].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
			} else if (orientation == 1) {
				for (int i = row; i < row + 1; i++) {
					if (col - 1 == 0 || !board[i][col - 2].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col + 1 == 9 || !board[i][col + 2].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
			} else if (orientation == 2) {
				for (int i = row; i < row + 3; i++) {
					if (col == 0 || !board[i][col - 1].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col == 8 || !board[row + 1][col + 2].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
			} else if (orientation == 3) {
				for (int i = row; i < row + 2; i++) {
					if (col - 1 == 0 || !board[i][col - 2].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col == 8 || !board[row][col + 2].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					} else if (!board[row + 1][col].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
			}
			//move left
			if (event.getCode() == KeyCode.LEFT) {
				if (orientation == 0) {
					if (col != 0 && emptyLeft) {
						board[row][col - 2].setFill(Color.BLUE);
						board[row + 1][col - 1].setFill(Color.BLUE);
						board[row + 2][col - 1].setFill(Color.BLUE);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col].setFill(Color.BLACK);
						col--;
					}
				} else if (orientation == 1) {
					// TODO empty left
					if (emptyLeft) {
						board[row][col - 2].setFill(Color.BLUE);
						board[row - 1][col].setFill(Color.BLUE);

						board[row][col + 1].setFill(Color.BLACK);
						board[row - 1][col + 1].setFill(Color.BLACK);
						col--;
					}
				} else if (orientation == 2) {
					// TODO empty left
					if (emptyLeft) {
						board[row - 1][col - 1].setFill(Color.BLUE);
						board[row][col - 1].setFill(Color.BLUE);
						board[row + 1][col - 1].setFill(Color.BLUE);

						board[row - 1][col].setFill(Color.BLACK);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col + 1].setFill(Color.BLACK);
						col--;
					}
				} else if (orientation == 3) {
					if (emptyLeft) {
						board[row][col - 2].setFill(Color.BLUE);
						board[row + 1][col - 2].setFill(Color.BLUE);

						board[row][col + 1].setFill(Color.BLACK);
						board[row + 1][col - 1].setFill(Color.BLACK);
						col--;
					}
				}

			}
			//move right
			if (event.getCode() == KeyCode.RIGHT) {

				if (orientation == 0) {
					if (col != 9 && emptyRight) {
						board[row][col + 1].setFill(Color.BLUE);
						board[row + 1][col + 1].setFill(Color.BLUE);
						board[row + 2][col + 1].setFill(Color.BLUE);
						board[row][col - 1].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col].setFill(Color.BLACK);
						col++;
					}
				}
				// TODO emptyRights
				if (orientation == 1) {
					if (emptyRight) {
						board[row][col + 2].setFill(Color.BLUE);
						board[row - 1][col + 2].setFill(Color.BLUE);

						board[row][col - 1].setFill(Color.BLACK);
						board[row - 1][col + 1].setFill(Color.BLACK);

						col++;
					}
				}
				if (orientation == 2) {
					if (emptyRight) {
						board[row][col + 1].setFill(Color.BLUE);
						board[row - 1][col + 1].setFill(Color.BLUE);
						board[row + 1][col + 2].setFill(Color.BLUE);

						board[row - 1][col].setFill(Color.BLACK);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						col++;
					}
				}
				if (orientation == 3) {
					if (emptyRight) {
						board[row][col + 2].setFill(Color.BLUE);
						board[row + 1][col].setFill(Color.BLUE);

						board[row][col - 1].setFill(Color.BLACK);
						board[row + 1][col - 1].setFill(Color.BLACK);

						col++;
					}
				}
			}
			//rotate
			if (event.getCode() == KeyCode.UP) {
				if (canRotate(board)) {
					rotateLShape(board);
				}
			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
	}
	/**
	 * rotates lshape
	 * @param board the board
	 */
	public void rotateLShape(Rectangle[][] board) {
		if (orientation == 0) {
			board[row][col].setFill(Color.BLACK);
			board[row][col - 1].setFill(Color.BLACK);
			board[row + 1][col].setFill(Color.BLACK);
			board[row + 2][col].setFill(Color.BLACK);
			row++;
			orientation++;
			
		} else if (orientation == 1) {
			board[row][col].setFill(Color.BLACK);
			board[row][col - 1].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			board[row - 1][col + 1].setFill(Color.BLACK);

			orientation++;
		} else if (orientation == 2) {
			board[row][col].setFill(Color.BLACK);
			board[row - 1][col].setFill(Color.BLACK);
			board[row + 1][col].setFill(Color.BLACK);
			board[row + 1][col + 1].setFill(Color.BLACK);

			orientation++;
		} else if (orientation == 3) {
			board[row][col].setFill(Color.BLACK);
			board[row][col - 1].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			board[row + 1][col - 1].setFill(Color.BLACK);

			row--;
			orientation = 0;
		}
	}
	/**
	 * checks if it can rotate the lshape
	 * @param board the board
	 * @return true if you can rotate lshape
	 */
	public boolean canRotate(Rectangle[][] board) {
		boolean canRotate = true;
		if (lShape) {
			if (orientation == 0) {
				if (col == 9) {
					canRotate = false;
				}
			}
			if (orientation == 1) {
				canRotate = true;
			}
			if (orientation == 2) {
				if (col == 0) {
					canRotate = false;
				}
			}
			if (orientation == 3) {
				canRotate = true;
			}
		}
		return canRotate;
	}

	/**
	 * creates jshape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void jShape(GridPane gridpane, Rectangle[][] board) {
		if (orientation == 0) {// if pos 0
			board[row][col].setFill(Color.RED);
			board[row + 1][col].setFill(Color.RED);
			board[row + 2][col].setFill(Color.RED);
			board[row][col + 1].setFill(Color.RED);
		} else if (orientation == 1) { // if pos 1
			board[row][col].setFill(Color.RED);
			board[row][col + 1].setFill(Color.RED);
			board[row + 1][col + 1].setFill(Color.RED);
			board[row][col - 1].setFill(Color.RED);
		} else if (orientation == 2) {// if pos 2
			board[row - 1][col].setFill(Color.RED);
			board[row][col].setFill(Color.RED);
			board[row + 1][col].setFill(Color.RED);
			board[row + 1][col - 1].setFill(Color.RED);
		} else if (orientation == 3) {// if pos 3
			board[row - 1][col - 1].setFill(Color.RED);
			board[row][col - 1].setFill(Color.RED);
			board[row][col].setFill(Color.RED);
			board[row][col + 1].setFill(Color.RED);
		}

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);

			boolean canRotate = true;
			if(row<1) {
				return;
			}
			if (orientation == 0) {
				if (col < 1) {
					canRotate = false;
				} else {
					if (!board[row + 1][col + 1].getFill().equals(Color.BLACK)
							|| !board[row - 1][col + 1].getFill().equals(Color.BLACK)
							|| !board[row + 2][col + 1].getFill().equals(Color.BLACK)) {
						canRotate = false;
					}
				}
			} else if (orientation == 1) {
				if (col < 1) {
					canRotate = false;
				} else {
					if (!board[row - 1][col].getFill().equals(Color.BLACK)
							|| !board[row + 1][col - 1].getFill().equals(Color.BLACK)) {
						canRotate = false;
					}
				}
			} else if (orientation == 2) {
				if (col == 9) {
					canRotate = false;
				} else {
					if (!board[row - 1][col - 1].getFill().equals(Color.BLACK)
							|| !board[row][col - 1].getFill().equals(Color.BLACK)
							|| !board[row][col + 1].getFill().equals(Color.BLACK)) {
						canRotate = false;
					}
				}
			} else if (orientation == 3) {
				if (!board[row - 1][col].getFill().equals(Color.BLACK)
						|| !board[row - 1][col + 1].getFill().equals(Color.BLACK)
						|| !board[row + 1][col].getFill().equals(Color.BLACK)) {
					canRotate = false;
				}
			}

			boolean emptyLeft = true;
			boolean emptyRight = true;
			if (orientation == 0) {
				for (int i = row; i < row + 3; i++) {
					if (col == 0 || !board[i][col - 1].getFill().equals(Color.BLACK)) {
						emptyLeft = false;
					}
					if (col + 1 == 9 || !board[i][col + 2].getFill().equals(Color.BLACK)) {
						emptyRight = false;
					}
				}
			} else if (orientation == 1) {
				if (col - 1 == 0 || !board[row][col - 2].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col + 1 == 9 || !board[row][col + 2].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			} else if (orientation == 2) {
				if (col - 1 == 0 || !board[row + 1][col - 2].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col == 9 || !board[row][col + 1].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			} else if (orientation == 3) {
				if (col - 1 == 0 || !board[row][col - 2].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col + 1 == 9 || !board[row][col + 2].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			}
			//moves left and right for each orientation
			if (orientation == 0) {
				if (event.getCode() == KeyCode.LEFT) {
					if (col != 0 && emptyLeft) {
						board[row][col - 1].setFill(Color.RED);
						board[row + 1][col - 1].setFill(Color.RED);
						board[row + 2][col - 1].setFill(Color.RED);
						board[row][col + 1].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col].setFill(Color.BLACK);
						col--;
					}
				}
				if (event.getCode() == KeyCode.RIGHT) {
					if (col != 9 && emptyRight) {
						board[row][col + 2].setFill(Color.RED);
						board[row + 1][col + 1].setFill(Color.RED);
						board[row + 2][col + 1].setFill(Color.RED);

						board[row][col].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						board[row + 2][col].setFill(Color.BLACK);
						col++;
					}
				}
			} else if (orientation == 1) {
				if (event.getCode() == KeyCode.LEFT) {
					if (col != 0 && emptyLeft) {
						board[row][col - 2].setFill(Color.RED);
						board[row + 1][col].setFill(Color.RED);
						board[row][col + 1].setFill(Color.BLACK);
						board[row + 1][col + 1].setFill(Color.BLACK);
						col--;
					}
				}
				if (event.getCode() == KeyCode.RIGHT) {
					if (col != 9 && emptyRight) {
						board[row][col + 2].setFill(Color.RED);
						board[row + 1][col + 2].setFill(Color.RED);
						board[row + 1][col + 1].setFill(Color.BLACK);
						board[row][col - 1].setFill(Color.BLACK);
						col++;
					}
				}
			} else if (orientation == 2) {
				if (event.getCode() == KeyCode.LEFT) {
					if (col != 0 && emptyLeft) {
						board[row - 1][col - 1].setFill(Color.RED);
						board[row][col - 1].setFill(Color.RED);
						board[row + 1][col - 2].setFill(Color.RED);
						board[row - 1][col].setFill(Color.BLACK);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						col--;
					}
				}
				if (event.getCode() == KeyCode.RIGHT) {
					if (col != 9 && emptyRight) {
						board[row - 1][col + 1].setFill(Color.RED);
						board[row + 1][col - 1].setFill(Color.RED);
						board[row][col + 1].setFill(Color.RED);
						board[row + 1][col + 1].setFill(Color.RED);
						board[row - 1][col].setFill(Color.BLACK);
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col - 1].setFill(Color.BLACK);
						col++;
					}
				}
			} else if (orientation == 3) {
				if (event.getCode() == KeyCode.LEFT) {
					if (col != 0 && emptyLeft) {
						board[row][col - 2].setFill(Color.RED);
						board[row - 1][col - 2].setFill(Color.RED);
						board[row - 1][col - 2].setFill(Color.RED);
						board[row - 1][col - 1].setFill(Color.BLACK);
						board[row][col + 1].setFill(Color.BLACK);
						col--;
					}
				}
				if (event.getCode() == KeyCode.RIGHT) {
					if (col != 9 && emptyRight) {
						board[row][col + 2].setFill(Color.RED);
						board[row - 1][col].setFill(Color.RED);
						board[row][col - 1].setFill(Color.BLACK);
						board[row - 1][col - 1].setFill(Color.BLACK);
						col++;
					}
				}
			}
			// rotates line when up key is pressed
			if (event.getCode() == KeyCode.UP) {
				if (canRotate) {
					rotateJShape(board);
				}

			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
		gridpane.requestFocus();
	}

	/**
	 * the rotates the jshape
	 * 
	 * @param board the board
	 */
	public void rotateJShape(Rectangle[][] board) {
		if (orientation == 0) {// if pos 0
			board[row][col].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			board[row + 1][col].setFill(Color.BLACK);
			board[row + 2][col].setFill(Color.BLACK);
			row++;
			orientation++;
		} else if (orientation == 1) {// if its position 1
			board[row][col - 1].setFill(Color.BLACK);
			board[row][col].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			board[row + 1][col + 1].setFill(Color.BLACK);
			orientation++;
		} else if (orientation == 2) {// if its position 2
			board[row - 1][col].setFill(Color.BLACK);
			board[row][col].setFill(Color.BLACK);
			board[row + 1][col].setFill(Color.BLACK);
			board[row + 1][col - 1].setFill(Color.BLACK);
			orientation++;
		} else if (orientation == 3) {
			board[row - 1][col - 1].setFill(Color.BLACK);
			board[row][col - 1].setFill(Color.BLACK);
			board[row][col].setFill(Color.BLACK);
			board[row][col + 1].setFill(Color.BLACK);
			orientation = 0;
		}
	}
	/**
	 * creates the tshape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void tShape(GridPane gridpane, Rectangle[][] board) {

		if (orientation == 0) {
			board[row][col].setFill(Color.PINK);
			board[row+1][col].setFill(Color.PINK);
			board[row+1][col+1].setFill(Color.PINK);
			board[row+1][col-1].setFill(Color.PINK);

		} 

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			
			boolean emptyLeft = true;
			boolean emptyRight = true;
			if(row<0) {
				return;
			}
			if (orientation == 0) {
				
				if (col - 1 == 0 || !board[row][col - 1].getFill().equals(Color.BLACK)||
					!board[row+1][col - 2].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col == 8 || !board[row][col + 1].getFill().equals(Color.BLACK)||
						!board[row+1][col + 2].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			}

			//move left
			if (event.getCode() == KeyCode.LEFT) {
				if (orientation == 0) {
					if (col != 1 && emptyLeft) {
						board[row][col - 1].setFill(Color.PINK);
						board[row + 1][col - 2].setFill(Color.PINK);
						
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col+1].setFill(Color.BLACK);						
						col--;
					}
				} 

			}
			//moves right
			if (event.getCode() == KeyCode.RIGHT) {

				if (orientation == 0) {
					if (col != 8 && emptyRight) {
						board[row][col + 1].setFill(Color.PINK);
						board[row + 1][col + 2].setFill(Color.PINK);
						
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col-1].setFill(Color.BLACK);
						
						col++;
					}
				}				
			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
	}
	/**
	 * makes the zshape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void zShape(GridPane gridpane, Rectangle[][] board) {

		if (orientation == 0) {
			board[row][col].setFill(Color.TEAL);
			board[row][col-1].setFill(Color.TEAL);
			board[row+1][col].setFill(Color.TEAL);
			board[row+1][col+1].setFill(Color.TEAL);
		} 

		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			
			boolean emptyLeft = true;
			boolean emptyRight = true;
			if(row<0) {
				return;
			}
			if (orientation == 0) {
				if (col - 1 == 0 || !board[row][col - 2].getFill().equals(Color.BLACK)||
					!board[row+1][col - 1].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col == 8 || !board[row][col + 1].getFill().equals(Color.BLACK)||
						!board[row+1][col + 2].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			}
			
			//moves left
			if (event.getCode() == KeyCode.LEFT) {
				if (orientation == 0) {
					if (col != 1 && emptyLeft) {
						board[row][col - 2].setFill(Color.TEAL);
						board[row + 1][col - 1].setFill(Color.TEAL);
						
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col+1].setFill(Color.BLACK);						
						col--;
					}
				} 

			}
			//moves right
			if (event.getCode() == KeyCode.RIGHT) {
				if (orientation == 0) {
					if (col != 8 && emptyRight) {
						board[row][col + 1].setFill(Color.TEAL);
						board[row + 1][col + 2].setFill(Color.TEAL);
						
						board[row][col-1].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);
						col++;
					}
				}
			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
	}
	/**
	 * creates the sShape and button movements
	 * @param gridpane the gridpane
	 * @param board the board
	 */
	public void sShape(GridPane gridpane, Rectangle[][] board) {

		if (orientation == 0) {
			board[row][col].setFill(Color.PURPLE);
			board[row][col+1].setFill(Color.PURPLE);
			board[row+1][col].setFill(Color.PURPLE);
			board[row+1][col-1].setFill(Color.PURPLE);

		} 
		// when the user presses left and right, move the rectangle
		gridpane.setOnKeyPressed(event -> {
			System.out.println(event);
			
			boolean emptyLeft = true;
			boolean emptyRight = true;
			if(row<0) {
				return;
			}
			if (orientation == 0) {
				
				if (col - 1 == 0 || !board[row][col - 1].getFill().equals(Color.BLACK)||
					!board[row+1][col - 2].getFill().equals(Color.BLACK)) {
					emptyLeft = false;
				}
				if (col == 8 || !board[row][col + 2].getFill().equals(Color.BLACK)||
						!board[row+1][col + 1].getFill().equals(Color.BLACK)) {
					emptyRight = false;
				}
			}
			
			//moves left
			if (event.getCode() == KeyCode.LEFT) {
				if (orientation == 0) {
					if (col != 1 && emptyLeft) {
						board[row][col - 1].setFill(Color.PURPLE);
						board[row + 1][col - 2].setFill(Color.PURPLE);
						
						board[row][col+1].setFill(Color.BLACK);
						board[row + 1][col].setFill(Color.BLACK);						
						col--;
					}
				} 

			}
			//move right
			if (event.getCode() == KeyCode.RIGHT) {

				if (orientation == 0) {
					if (col != 8 && emptyRight) {
						board[row][col + 2].setFill(Color.PURPLE);
						board[row + 1][col + 1].setFill(Color.PURPLE);
						
						board[row][col].setFill(Color.BLACK);
						board[row + 1][col-1].setFill(Color.BLACK);
						
						col++;
					}
				}
				
			}
			// TODO bounds checking
		});
		gridpane.requestFocus();
	}
	
	/**
	 * ends the game and updates the score
	 */
	public void endGame() {
		label.setText("Level: " + level + "\nScore: " + score + "\nLines: " + numLinesCleared + "\n\nGAME OVER");
		lost = true;
	}
	
	public static void main(String [] args) {
		Tetris tetrisObj = new Tetris();
		tetrisObj.startTetris();
	}
}
