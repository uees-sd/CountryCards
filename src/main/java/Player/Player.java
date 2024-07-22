/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Player;

/**
 *
 * @author josev
 */
public class Player {
    private String name;
    private int movements;

    public Player(String name, int movements) {
        this.name = name;
        this.movements = movements;
    }

    public String getName() {
        return name;
    }

    public int getMovements() {
        return movements;
    }

    public void incrementMovements() {
        movements++;
    }
}