package game.controllers.examples;

import java.awt.Color;
import java.awt.Point;

import game.controllers.GhostController;
import game.core.G;
import game.core.Game;
import game.core.Game.DM;
import game.core.GameView;

public final class VidhurGhost implements GhostController {
	boolean debugging;
	int redGhostTarget, pinkGhostTarget, orangeGhostTarget, blueGhostTarget;
	int redGhost = 0, pinkGhost = 1, orangeGhost = 2, blueGhost = 3;

	public VidhurGhost(boolean debug) {
		debugging = debug;
	}

	public void getHighest(Game game) {
		int index = 0;
		int indexx = 0, indexy = 0;
		for (int i = 0; i < game.getNumberOfNodes(); i++) {
			if (game.getX(i) >= indexx)
				indexx = game.getX(i);
			if (game.getY(i) >= indexy)
				indexy = game.getY(i);

		}

		System.out.println("index: " + index + " x:" + indexx + " " + " y: "
				+ indexy);
	}

	public int[] getActions(Game game, long timeDue) {
		long start = System.currentTimeMillis();
		// getHighest(game);
		// messWithNodes(game);
		int[] directions = new int[Game.NUM_GHOSTS];

		for (int i = 0; i < directions.length; i++)
			if (game.ghostRequiresAction(i)) {
				if (i == 0) {
					directions[0] = RedGhost(game);
				} else if (i == 1) {
					directions[1] = PinkGhost(game);
				} else if (i == 2) {
					directions[2] = OrangeGhost(game);
				} else if (i == 3) {
					directions[3] = BlueGhost(game);
				} else
					System.out.println("Not supposed to be here");
			}
		if (debugging) {
			// adding any line drawing
			GameView.addLines(game, Color.orange,
					game.getCurGhostLoc(orangeGhost), orangeGhostTarget);
			GameView.addLines(game, Color.RED, game.getCurGhostLoc(redGhost),
					redGhostTarget);
			GameView.addLines(game, Color.blue, game.getCurGhostLoc(blueGhost),
					blueGhostTarget);
			GameView.addLines(game, Color.pink, game.getCurGhostLoc(pinkGhost),
					pinkGhostTarget);
			GameView.addLines(game, Color.WHITE,
					game.getCurGhostLoc(redGhost), this.findNodeWithXY(this.aheadOfPacMan(game, 2).x,this.aheadOfPacMan(game, 2).y,game));

			if (flag)
				flag = false;
		}
		long end = System.currentTimeMillis();
		//

		if ((end - start) > 3) {
			for (int i = 0; i < directions.length; i++)
				if (game.ghostRequiresAction(i)) {
					System.out.print(i + " ");
				}
			System.out.println("time: " + (end - start));
		}

		// printArray(directions);
		return directions;
	}

	public int RedGhost(Game game) { // just goes to the node where pacman
										// currently is
		redGhostTarget = game.getCurPacManLoc();
		return game.getNextGhostDir(0, redGhostTarget, true, DM.EUCLID);
	}

	public int PinkGhost(Game game) {
		Point point = aheadOfPacMan(game, 4);
		int x = point.x;
		int y = point.y;
		pinkGhostTarget = this.findNodeWithXY(x, y, game);
		return game
				.getNextGhostDir(pinkGhost, pinkGhostTarget, true, DM.EUCLID);
	}

	public int BlueGhost(Game game) {
		// find tile 2 spaces ahead of pacman, then double vector from red ghost
		Point point = aheadOfPacMan(game, 2);

		// Point p = new Point(game.getX(game.getCurPacManLoc()), game.getY(game
		// .getCurPacManLoc()));
		int redGhostLoc = game.getCurGhostLoc(redGhost);
		Point redGhostPoint = new Point(game.getX(redGhostLoc),
				game.getY(redGhostLoc));
		int calculatedX = 2 * (point.x - redGhostPoint.x) + point.x;
		int calculatedY = 2 * (point.y - redGhostPoint.y) + point.y;
		blueGhostTarget = this.findNodeWithXY(calculatedX, calculatedY, game);
		// Point target = new Point(game.getX(blueGhostTarget),
		// game.getY(blueGhostTarget));

		flag = true;
		return game
				.getNextGhostDir(blueGhost, blueGhostTarget, true, DM.EUCLID);

	}

	boolean flag = false;

	public int OrangeGhost(Game game) { // when close to pacman, return to
										// bottom left when not go to pacman

		double dist = game.getEuclideanDistance(
				game.getCurGhostLoc(orangeGhost), game.getCurPacManLoc());
		if (dist > 5 * 8) {
			orangeGhostTarget = game.getCurPacManLoc();
			return game.getNextGhostDir(0, game.getCurPacManLoc(), true,
					DM.EUCLID);
		} else { // scatter mode
			orangeGhostTarget = 1196;
			return game.getNextGhostDir(orangeGhost, 1196, true, DM.EUCLID);
		}
	}

	public int findNodeWithXY(int x, int y, Game game) {
		int maxX = 108;
		int maxY = 116;
		if (x > maxX)
			x = maxX;
		else if (x < 0)
			x = 0;
		if (y > maxY)
			y = maxY;
		else if (y < 0)
			y = 0;

		int index = 0;
		double minDist = 10;

		for (int i = 0; i < game.getNumberOfNodes(); i++) {
			int tempx, tempy;
			tempx = game.getX(i);
			tempy = game.getY(i);
			double tempDist = Math.pow(
					(Math.pow(tempx - x, 2) + Math.pow(tempy - y, 2)), .5);
			if (tempDist < minDist) {
				index = i;
				minDist = tempDist;
				if (minDist < 2)
					break;
			}

		}
		return index;
	}

	public void printArray(int[] arr) {
		StringBuffer s = new StringBuffer();
		for (int i : arr)
			s.append(Integer.toString(i));
		System.out.println(s);
	}

	public Point aheadOfPacMan(Game game, int num) {
		int currPacManLoc = game.getCurPacManLoc();
		int currPacManDir = game.getCurPacManDir();

		int x = game.getX(currPacManLoc);
		int y = game.getY(currPacManLoc);
		int diff = num * 5;
		switch (currPacManDir) {
		case 0:
			y = y - diff;
			break;
		case 1:
			x = x + diff;
			break;
		case 2:
			y = y + diff;
			break;
		case 3:
			x = x - diff;
			break;
		default:
			break;
		}
		return new Point(x, y);
	}

}