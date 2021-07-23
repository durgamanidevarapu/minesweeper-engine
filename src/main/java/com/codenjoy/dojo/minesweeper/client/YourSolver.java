package com.codenjoy.dojo.minesweeper.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.reflections.vfs.Vfs;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    static final int SIDE = 28;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
        Point p = board.getMe();
        if(board.isBarrierAt(p.getX(),p.getY())) return  Direction.RIGHT.toString();
        List<Elements> nearElements = board.getNear(p);

        System.out.println("near elements: " + nearElements);
        return getDirection(board);
    }

    public String getDirection(Board board){

        char[][] field = board.getField();

        int x = board.getMe().getX();
        int y = board.getMe().getY();
        System.out.println("point"+board.getMe());
        String up = moveUP(board);
        if(x == 1 && y == 1) return Direction.RIGHT.toString();
        if(x == 1 && y == 28)return Direction.UP.toString();
        if(x == 28 && y == 1)return Direction.DOWN.toString();
        if(x == 28 && y == 28)return Direction.LEFT.toString();
       if(board.getNear(board.getMe()).contains(Elements.NONE)) return Direction.random(dice).toString();



        return  getValue(board);
    }

    public String getValue(Board board){
        Point p = board.getMe();

        List<Elements> neighbours = board.getNear(board.getMe());
        int x = board.getMe().getX();
        int y = board.getMe().getY();
        char[][] field = board.getField();


        if(neighbours.contains(Elements.ONE_EFFECTED_PERSON) || neighbours.contains(Elements.TWO_EFFECTED_PERSONS)
                || neighbours.contains(Elements.THREE_EFFECTED_PERSONS) || neighbours.contains(Elements.FOUR_EFFECTED_PERSONS)
        || neighbours.contains(Elements.FIVE_EFFECTED_PERSONS) || neighbours.contains(Elements.SIX_EFFECTED_PERSONS)
        || neighbours.contains(Elements.SEVEN_EFFECTED_PERSONS) || neighbours.contains(Elements.EIGHT_EFFECTED_PERSONS)){
            String dir = getValidDirection(board);
            System.out.println("right:" + field[x+1][y]);
            if(field[x+1][y]==Elements.HIDDEN.ch())return Direction.RIGHT.ACT(true).toString();
            if(field[x-1][y]==Elements.HIDDEN.ch())return Direction.LEFT.ACT(true).toString();
            if(field[x][y-1]==Elements.HIDDEN.ch())return Direction.UP.ACT(true).toString();
            if(field[x][y-1]==Elements.HIDDEN.ch())return Direction.DOWN.ACT(true).toString();

            //if(field[x+1][y] == Elements.VACCINE.ch())return Direction.RIGHT.toString();
            /*board.set(x,y+1,Elements.VACCINE.ch());
            board.set(x,y-1,Elements.VACCINE.ch());
            board.set(x+1,y,Elements.VACCINE.ch());
            board.set(x+1,y-1,Elements.VACCINE.ch());
            board.set(x-1,y,Elements.VACCINE.ch());
            board.set(x+1,y+1,Elements.VACCINE.ch());
            board.set(x-1,y-1,Elements.VACCINE.ch());
            board.set(x-1,y+1,Elements.VACCINE.ch());
            board.set(x,y,Elements.VACCINE.ch());*/
            System.out.println(board.getNear(p));


            return Direction.UP.ACT(true).toString();
        }
        if(isValid(x,y-1)) return Direction.UP.toString();
        if(isValid(x,y+1)) return Direction.DOWN.toString();
        if(isValid(x-1,y)) return Direction.LEFT.toString();
        if(isValid(x+1,y))return Direction.RIGHT.toString();
        return Direction.random(dice).toString();

    }
    public String getValidDirection(Board board){
        int x = board.getMe().getY();
        int y = board.getMe().getX();
        Point p = board.getMe();
        String val = Direction.ACT(x,y-1);
        System.out.println(Direction.ACT.toString());
        return val;
    }
public String moveUP(Board board){
    int x = board.getMe().getX();
    int y = board.getMe().getY();
    char[][] field = board.getField();
        if(y-1!=1 && field[x][y-1]!=' ' && field[x][y-1]!=Elements.BORDER.ch() && field[x][y-1] != Elements.HERE_IS_VIRUS.ch() && field[x][y-1] == Elements.HIDDEN.ch())return Direction.UP.toString();
        return StringUtils.EMPTY;
}
    public String moveDown(Board board) {
        int x = board.getMe().getX();
        int y = board.getMe().getY();
        char[][] field = board.getField();
        if (y - 1 != 1 && field[x][y + 1] != ' ' && field[x][y + 1] != Elements.BORDER.ch() && field[x][y + 1] != Elements.HERE_IS_VIRUS.ch())
            return Direction.DOWN.toString();

        return StringUtils.EMPTY;
    }

    public boolean isValid(int row,int col){
        char[][] field = board.getField();
        return (row >= 1) && (row < SIDE) &&
                (col >= 1) && (col < SIDE) && field[row][col] !=' ';
    }
    boolean isMine (int row, int col, char field[][])
    {
        if (field[row][col] == '*')
            return (true);
        else
            return (false);
    }
    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://10.245.129.28:8080/codenjoy-contest/board/player/irnmnbhp1ex82l37rgnp?code=3539142878085958775",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
